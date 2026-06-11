package com.awsome.shop.product.application.impl.service.stock;

import com.awsome.shop.product.application.api.dto.stock.ReserveStockResponse;
import com.awsome.shop.product.application.api.dto.stock.request.ReservationRequest;
import com.awsome.shop.product.application.api.dto.stock.request.ReserveStockRequest;
import com.awsome.shop.product.application.api.service.stock.StockApplicationService;
import com.awsome.shop.product.domain.service.stock.StockDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 库存应用服务实现
 */
@Service
@RequiredArgsConstructor
public class StockApplicationServiceImpl implements StockApplicationService {

    private final StockDomainService stockDomainService;

    @Override
    public ReserveStockResponse reserve(ReserveStockRequest request) {
        String reservationId = stockDomainService.reserve(
                request.getProductId(), request.getQuantity(), request.getOrderRef());
        return new ReserveStockResponse(reservationId);
    }

    @Override
    public void release(ReservationRequest request) {
        stockDomainService.release(request.getReservationId());
    }

    @Override
    public void confirm(ReservationRequest request) {
        stockDomainService.confirm(request.getReservationId());
    }
}
