package com.awsome.shop.product.facade.http.controller;

import com.awsome.shop.product.application.api.dto.stock.ReserveStockResponse;
import com.awsome.shop.product.application.api.dto.stock.request.ReservationRequest;
import com.awsome.shop.product.application.api.dto.stock.request.ReserveStockRequest;
import com.awsome.shop.product.application.api.service.stock.StockApplicationService;
import com.awsome.shop.product.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 库存内部接口 Controller（供 order-service 经服务间调用，FR-O3/FR-O6）
 *
 * <p>路径前缀 /api/v1/private/**：仅限内部服务访问，网关不对外暴露，
 * 且应在部署层限制 8002 端口仅被 order-service 可达。</p>
 */
@Tag(name = "Stock(Internal)", description = "库存内部接口")
@RestController
@RequestMapping("/api/v1/private/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockApplicationService stockApplicationService;

    @Operation(summary = "预占库存")
    @PostMapping("/reserve")
    public Result<ReserveStockResponse> reserve(@RequestBody @Valid ReserveStockRequest request) {
        return Result.success(stockApplicationService.reserve(request));
    }

    @Operation(summary = "释放预占（取消/补偿）")
    @PostMapping("/release")
    public Result<Void> release(@RequestBody @Valid ReservationRequest request) {
        stockApplicationService.release(request);
        return Result.success();
    }

    @Operation(summary = "预占转正式扣减（发货）")
    @PostMapping("/confirm")
    public Result<Void> confirm(@RequestBody @Valid ReservationRequest request) {
        stockApplicationService.confirm(request);
        return Result.success();
    }
}
