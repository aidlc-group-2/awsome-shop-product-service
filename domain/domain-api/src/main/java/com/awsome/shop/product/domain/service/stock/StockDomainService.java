package com.awsome.shop.product.domain.service.stock;

/**
 * 库存领域服务接口（供 order-service 经 private 接口调用）
 *
 * <p>采用预占模型，配合 order-service 的 Saga：
 * reserve（下单预占）→ confirm（发货正式扣减）/ release（取消补偿回补）。
 * 所有操作以 reservationId / orderRef 为幂等键，可安全重试。</p>
 */
public interface StockDomainService {

    /**
     * 下单预占库存。库存充足时扣减可用库存并落预占记录。
     *
     * <p>幂等：同一 (orderRef, productId) 重复调用返回首次的 reservationId，不重复扣减。</p>
     *
     * @return 预占凭据 reservationId
     */
    String reserve(Long productId, int quantity, String orderRef);

    /**
     * 释放预占（取消 / Saga 补偿），回补库存。
     *
     * <p>幂等：已释放则直接返回；已确认（发货）不可释放。</p>
     */
    void release(String reservationId);

    /**
     * 预占转正式扣减（发货），库存不回补，累加已兑换数量。
     *
     * <p>幂等：已确认则直接返回；已释放不可确认。</p>
     */
    void confirm(String reservationId);
}
