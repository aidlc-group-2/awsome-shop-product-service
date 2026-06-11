package com.awsome.shop.product.repository.mysql.impl.stock;

import com.awsome.shop.product.domain.model.stock.StockReservationEntity;
import com.awsome.shop.product.repository.mysql.mapper.stock.StockReservationMapper;
import com.awsome.shop.product.repository.mysql.po.stock.StockReservationPO;
import com.awsome.shop.product.repository.stock.StockReservationRepository;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 库存预占记录仓储实现
 */
@Repository
@RequiredArgsConstructor
public class StockReservationRepositoryImpl implements StockReservationRepository {

    private final StockReservationMapper stockReservationMapper;

    @Override
    public StockReservationEntity getByReservationId(String reservationId) {
        LambdaQueryWrapper<StockReservationPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StockReservationPO::getReservationId, reservationId);
        StockReservationPO po = stockReservationMapper.selectOne(wrapper);
        return po == null ? null : toEntity(po);
    }

    @Override
    public StockReservationEntity getByOrderRefAndProduct(String orderRef, Long productId) {
        LambdaQueryWrapper<StockReservationPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StockReservationPO::getOrderRef, orderRef)
                .eq(StockReservationPO::getProductId, productId);
        StockReservationPO po = stockReservationMapper.selectOne(wrapper);
        return po == null ? null : toEntity(po);
    }

    @Override
    public void save(StockReservationEntity entity) {
        StockReservationPO po = new StockReservationPO();
        po.setReservationId(entity.getReservationId());
        po.setOrderRef(entity.getOrderRef());
        po.setProductId(entity.getProductId());
        po.setQuantity(entity.getQuantity());
        po.setStatus(entity.getStatus());
        stockReservationMapper.insert(po);
        entity.setId(po.getId());
    }

    @Override
    public int updateStatusCas(String reservationId, String expectedStatus, String newStatus) {
        LambdaUpdateWrapper<StockReservationPO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(StockReservationPO::getReservationId, reservationId)
                .eq(StockReservationPO::getStatus, expectedStatus)
                .set(StockReservationPO::getStatus, newStatus);
        return stockReservationMapper.update(null, wrapper);
    }

    private StockReservationEntity toEntity(StockReservationPO po) {
        StockReservationEntity entity = new StockReservationEntity();
        entity.setId(po.getId());
        entity.setReservationId(po.getReservationId());
        entity.setOrderRef(po.getOrderRef());
        entity.setProductId(po.getProductId());
        entity.setQuantity(po.getQuantity());
        entity.setStatus(po.getStatus());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }
}
