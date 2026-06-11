package com.awsome.shop.product.application.api.dto.stock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 库存预占响应
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReserveStockResponse {

    /**
     * 预占凭据，供后续释放 / 确认使用
     */
    private String reservationId;
}
