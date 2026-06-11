package com.awsome.shop.product.domain.model.stock;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 库存预占领域实体
 *
 * <p>状态流转：RESERVED →(发货) CONFIRMED / RESERVED →(取消补偿) RELEASED。
 * CONFIRMED / RELEASED 为终态，不可再变更。</p>
 */
@Data
public class StockReservationEntity {

    /** 已预占，库存已从可用量中扣减但未正式售出 */
    public static final String STATUS_RESERVED = "RESERVED";
    /** 已确认，预占转为正式扣减（发货） */
    public static final String STATUS_CONFIRMED = "CONFIRMED";
    /** 已释放，预占回补库存（取消 / Saga 补偿） */
    public static final String STATUS_RELEASED = "RELEASED";

    private Long id;

    private String reservationId;

    private String orderRef;

    private Long productId;

    private Integer quantity;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public boolean isReserved() {
        return STATUS_RESERVED.equals(this.status);
    }

    public boolean isConfirmed() {
        return STATUS_CONFIRMED.equals(this.status);
    }

    public boolean isReleased() {
        return STATUS_RELEASED.equals(this.status);
    }
}
