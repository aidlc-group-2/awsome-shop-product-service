package com.awsome.shop.product.application.api.dto.stock.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 按预占凭据操作的请求（释放 / 确认）
 */
@Data
public class ReservationRequest {

    @NotBlank(message = "预占凭据不能为空")
    private String reservationId;
}
