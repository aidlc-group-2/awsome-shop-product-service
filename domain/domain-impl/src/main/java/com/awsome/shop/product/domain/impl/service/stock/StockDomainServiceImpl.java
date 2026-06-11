package com.awsome.shop.product.domain.impl.service.stock;

import com.awsome.shop.product.common.enums.StockErrorCode;
import com.awsome.shop.product.common.exception.BusinessException;
import com.awsome.shop.product.domain.model.stock.StockReservationEntity;
import com.awsome.shop.product.domain.service.stock.StockDomainService;
import com.awsome.shop.product.repository.product.ProductRepository;
import com.awsome.shop.product.repository.stock.StockReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * 库存领域服务实现（FR-O3/FR-O6）
 *
 * <p>防超卖依赖条件 UPDATE（{@code stock >= quantity}），幂等依赖
 * stock_reservation 表的 (order_ref, product_id) 唯一约束与 reservationId 状态机。</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockDomainServiceImpl implements StockDomainService {

    private final ProductRepository productRepository;
    private final StockReservationRepository reservationRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String reserve(Long productId, int quantity, String orderRef) {
        if (quantity <= 0) {
            throw new BusinessException(StockErrorCode.INVALID_QUANTITY);
        }

        // 幂等：同一订单+商品已预占过，直接返回首次凭据，不重复扣减
        StockReservationEntity existing = reservationRepository.getByOrderRefAndProduct(orderRef, productId);
        if (existing != null) {
            log.info("[FR-O3] 库存预占幂等命中 orderRef={} productId={} reservationId={} status={}",
                    orderRef, productId, existing.getReservationId(), existing.getStatus());
            return existing.getReservationId();
        }

        // 校验商品存在
        if (productRepository.getById(productId) == null) {
            throw new BusinessException(StockErrorCode.PRODUCT_NOT_FOUND, productId);
        }

        // 条件扣减：仅当库存充足时成功，杜绝超卖
        boolean deducted = productRepository.deductStock(productId, quantity);
        if (!deducted) {
            throw new BusinessException(StockErrorCode.INSUFFICIENT_STOCK, productId, quantity);
        }

        String reservationId = "RSV-" + UUID.randomUUID();
        StockReservationEntity reservation = new StockReservationEntity();
        reservation.setReservationId(reservationId);
        reservation.setOrderRef(orderRef);
        reservation.setProductId(productId);
        reservation.setQuantity(quantity);
        reservation.setStatus(StockReservationEntity.STATUS_RESERVED);
        try {
            reservationRepository.save(reservation);
        } catch (DuplicateKeyException e) {
            // 并发下另一线程已为同一 (orderRef, productId) 抢先落记录：回滚本次扣减，返回已存在的凭据
            log.warn("[FR-O3] 预占记录并发冲突，回滚本次扣减 orderRef={} productId={}", orderRef, productId, e);
            throw new BusinessException(StockErrorCode.INSUFFICIENT_STOCK, productId, quantity, e);
        }

        log.info("[FR-O3] 库存预占成功 orderRef={} productId={} qty={} reservationId={}",
                orderRef, productId, quantity, reservationId);
        return reservationId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void release(String reservationId) {
        StockReservationEntity reservation = reservationRepository.getByReservationId(reservationId);
        if (reservation == null) {
            throw new BusinessException(StockErrorCode.RESERVATION_NOT_FOUND, reservationId);
        }
        if (reservation.isReleased()) {
            log.info("[FR-O3] 预占已释放(幂等) reservationId={}", reservationId);
            return;
        }
        if (reservation.isConfirmed()) {
            throw new BusinessException(StockErrorCode.RESERVATION_STATE_INVALID,
                    reservationId, reservation.getStatus());
        }

        // CAS 状态机：仅当仍为 RESERVED 才回补，防止与并发 confirm 重复操作
        int updated = reservationRepository.updateStatusCas(reservationId,
                StockReservationEntity.STATUS_RESERVED, StockReservationEntity.STATUS_RELEASED);
        if (updated == 0) {
            log.info("[FR-O3] 预占状态已被并发变更，跳过回补 reservationId={}", reservationId);
            return;
        }
        productRepository.restoreStock(reservation.getProductId(), reservation.getQuantity());
        log.info("[FR-O3] 库存预占释放成功 reservationId={} productId={} qty={}",
                reservationId, reservation.getProductId(), reservation.getQuantity());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(String reservationId) {
        StockReservationEntity reservation = reservationRepository.getByReservationId(reservationId);
        if (reservation == null) {
            throw new BusinessException(StockErrorCode.RESERVATION_NOT_FOUND, reservationId);
        }
        if (reservation.isConfirmed()) {
            log.info("[FR-O6] 预占已确认(幂等) reservationId={}", reservationId);
            return;
        }
        if (reservation.isReleased()) {
            throw new BusinessException(StockErrorCode.RESERVATION_STATE_INVALID,
                    reservationId, reservation.getStatus());
        }

        int updated = reservationRepository.updateStatusCas(reservationId,
                StockReservationEntity.STATUS_RESERVED, StockReservationEntity.STATUS_CONFIRMED);
        if (updated == 0) {
            log.info("[FR-O6] 预占状态已被并发变更，跳过确认 reservationId={}", reservationId);
            return;
        }
        // 库存在预占时已扣减，确认只累加已兑换数量
        productRepository.increaseSoldCount(reservation.getProductId(), reservation.getQuantity());
        log.info("[FR-O6] 库存正式扣减成功 reservationId={} productId={} qty={}",
                reservationId, reservation.getProductId(), reservation.getQuantity());
    }
}
