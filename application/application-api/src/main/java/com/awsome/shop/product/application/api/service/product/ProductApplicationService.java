package com.awsome.shop.product.application.api.service.product;

import com.awsome.shop.product.application.api.dto.product.ProductDTO;
import com.awsome.shop.product.application.api.dto.product.ProductSnapshotDTO;
import com.awsome.shop.product.application.api.dto.product.request.CreateProductRequest;
import com.awsome.shop.product.application.api.dto.product.request.ListProductRequest;
import com.awsome.shop.product.common.dto.PageResult;

/**
 * Product 应用服务接口
 */
public interface ProductApplicationService {

    PageResult<ProductDTO> list(ListProductRequest request);

    ProductDTO create(CreateProductRequest request);

    /**
     * 获取商品权威快照（供 order-service 下单核价）。商品不存在时抛业务异常。
     */
    ProductSnapshotDTO getSnapshot(Long productId);
}
