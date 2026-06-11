package com.awsome.shop.product.application.api.service.stock;

import com.awsome.shop.product.application.api.dto.stock.ReserveStockResponse;
import com.awsome.shop.product.application.api.dto.stock.request.ReservationRequest;
import com.awsome.shop.product.application.api.dto.stock.request.ReserveStockRequest;

/**
 * 库存应用服务接口（供 order-service 内部调用）
 */
public interface StockApplicationService {

    ReserveStockResponse reserve(ReserveStockRequest request);

    void release(ReservationRequest request);

    void confirm(ReservationRequest request);
}
