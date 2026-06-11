package com.awsome.shop.product.application.api.dto.stock.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 库存预占请求（order-service 下单调用）
 */
@Data
public class ReserveStockRequest {

    @NotNull(message = "商品ID不能为空")
    private Long productId;

    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量必须大于 0")
    @Max(value = 9999, message = "数量超出上限")
    private Integer quantity;

    @NotBlank(message = "订单号不能为空")
    private String orderRef;
}
