package com.awsome.shop.product.repository.stock;

import com.awsome.shop.product.domain.model.stock.StockReservationEntity;

/**
 * 库存预占记录仓储接口
 */
public interface StockReservationRepository {

    /**
     * 按预占凭据查询。
     */
    StockReservationEntity getByReservationId(String reservationId);

    /**
     * 按订单号 + 商品查询（下单幂等判断）。
     */
    StockReservationEntity getByOrderRefAndProduct(String orderRef, Long productId);

    /**
     * 新增预占记录。并发下若 (orderRef, productId) 撞唯一键，由调用方处理 DuplicateKeyException。
     */
    void save(StockReservationEntity entity);

    /**
     * 仅当当前状态为 expectedStatus 时，将预占记录更新为 newStatus（乐观状态机 CAS）。
     *
     * @return 受影响行数（0 表示状态已变更，应视为并发冲突或重复操作）
     */
    int updateStatusCas(String reservationId, String expectedStatus, String newStatus);
}
