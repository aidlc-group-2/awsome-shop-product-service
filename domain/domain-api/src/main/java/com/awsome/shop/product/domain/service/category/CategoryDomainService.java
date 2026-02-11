package com.awsome.shop.product.domain.service.category;

import com.awsome.shop.product.domain.model.category.CategoryEntity;

import java.util.List;

/**
 * Category 领域服务接口
 */
public interface CategoryDomainService {

    List<CategoryEntity> list(String name, Integer status);
}
