package com.awsome.shop.product.domain.impl.service.category;

import com.awsome.shop.product.domain.model.category.CategoryEntity;
import com.awsome.shop.product.domain.service.category.CategoryDomainService;
import com.awsome.shop.product.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Category 领域服务实现
 */
@Service
@RequiredArgsConstructor
public class CategoryDomainServiceImpl implements CategoryDomainService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryEntity> list(String name, Integer status) {
        return categoryRepository.listAll(name, status);
    }
}
