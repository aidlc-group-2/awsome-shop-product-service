package com.awsome.shop.product.domain.impl.service.category;

import com.awsome.shop.product.domain.model.category.CategoryEntity;
import com.awsome.shop.product.repository.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * CategoryDomainServiceImpl 单元测试
 */
@ExtendWith(MockitoExtension.class)
class CategoryDomainServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryDomainServiceImpl categoryDomainService;

    @Test
    @DisplayName("list 应将条件透传给仓储 listAll")
    void listShouldDelegateToRepository() {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(1L);
        when(categoryRepository.listAll("数码", 1)).thenReturn(List.of(entity));

        List<CategoryEntity> result = categoryDomainService.list("数码", 1);

        assertThat(result).containsExactly(entity);
        verify(categoryRepository).listAll("数码", 1);
    }

    @Test
    @DisplayName("list 条件为 null 时也应透传")
    void listShouldPassNullConditions() {
        when(categoryRepository.listAll(null, null)).thenReturn(Collections.emptyList());

        List<CategoryEntity> result = categoryDomainService.list(null, null);

        assertThat(result).isEmpty();
        verify(categoryRepository).listAll(null, null);
    }
}
