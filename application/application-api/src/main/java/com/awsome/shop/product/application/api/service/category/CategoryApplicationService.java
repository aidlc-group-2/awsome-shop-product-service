package com.awsome.shop.product.application.api.service.category;

import com.awsome.shop.product.application.api.dto.category.CategoryDTO;
import com.awsome.shop.product.application.api.dto.category.request.ListCategoryRequest;

import java.util.List;

/**
 * Category 应用服务接口
 */
public interface CategoryApplicationService {

    List<CategoryDTO> list(ListCategoryRequest request);
}
