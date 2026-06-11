package com.awsome.shop.product.application.impl.service.category;

import com.awsome.shop.product.application.api.dto.category.CategoryDTO;
import com.awsome.shop.product.application.api.dto.category.request.ListCategoryRequest;
import com.awsome.shop.product.domain.model.category.CategoryEntity;
import com.awsome.shop.product.domain.service.category.CategoryDomainService;
import com.awsome.shop.product.domain.service.product.ProductDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * CategoryApplicationServiceImpl 单元测试
 */
@ExtendWith(MockitoExtension.class)
class CategoryApplicationServiceImplTest {

    @Mock
    private CategoryDomainService categoryDomainService;

    @Mock
    private ProductDomainService productDomainService;

    @InjectMocks
    private CategoryApplicationServiceImpl categoryApplicationService;

    private CategoryEntity category(Long id, String name, Long parentId, Integer sortOrder) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setParentId(parentId);
        entity.setSortOrder(sortOrder);
        entity.setStatus(1);
        return entity;
    }

    @Test
    @DisplayName("list 应构建父子树形结构")
    void listShouldBuildTree() {
        List<CategoryEntity> categories = List.of(
                category(1L, "数码", null, 10),
                category(2L, "手机", 1L, 5),
                category(3L, "电脑", 1L, 8),
                category(4L, "家居", null, 20));
        when(categoryDomainService.list(null, null)).thenReturn(categories);
        when(productDomainService.countGroupByCategory()).thenReturn(Collections.emptyMap());

        List<CategoryDTO> tree = categoryApplicationService.list(new ListCategoryRequest());

        assertThat(tree).hasSize(2);
        CategoryDTO digital = tree.stream().filter(d -> d.getId().equals(1L)).findFirst().orElseThrow();
        assertThat(digital.getChildren()).extracting(CategoryDTO::getId).containsExactly(3L, 2L);
        CategoryDTO home = tree.stream().filter(d -> d.getId().equals(4L)).findFirst().orElseThrow();
        assertThat(home.getChildren()).isEmpty();
    }

    @Test
    @DisplayName("根节点和子节点均按 sortOrder 降序、相同时按 id 升序")
    void listShouldSortBySortOrderDescThenIdAsc() {
        List<CategoryEntity> categories = List.of(
                category(1L, "A", null, 10),
                category(2L, "B", null, 20),
                category(3L, "C", null, 10),
                category(10L, "A-1", 1L, 1),
                category(11L, "A-2", 1L, 3),
                category(12L, "A-3", 1L, 3));
        when(categoryDomainService.list(any(), any())).thenReturn(categories);
        when(productDomainService.countGroupByCategory()).thenReturn(Collections.emptyMap());

        List<CategoryDTO> tree = categoryApplicationService.list(new ListCategoryRequest());

        // 根: B(20) > A(10,id=1) > C(10,id=3)
        assertThat(tree).extracting(CategoryDTO::getId).containsExactly(2L, 1L, 3L);
        // A 的子节点: A-2(3,id=11) > A-3(3,id=12) > A-1(1)
        CategoryDTO a = tree.get(1);
        assertThat(a.getChildren()).extracting(CategoryDTO::getId).containsExactly(11L, 12L, 10L);
    }

    @Test
    @DisplayName("商品数量应按分类名称关联，无匹配时为 0")
    void listShouldAttachProductCounts() {
        List<CategoryEntity> categories = List.of(
                category(1L, "数码", null, 10),
                category(2L, "家居", null, 5));
        when(categoryDomainService.list(any(), any())).thenReturn(categories);
        when(productDomainService.countGroupByCategory()).thenReturn(Map.of("数码", 7L));

        List<CategoryDTO> tree = categoryApplicationService.list(new ListCategoryRequest());

        CategoryDTO digital = tree.stream().filter(d -> d.getName().equals("数码")).findFirst().orElseThrow();
        CategoryDTO home = tree.stream().filter(d -> d.getName().equals("家居")).findFirst().orElseThrow();
        assertThat(digital.getProductCount()).isEqualTo(7L);
        assertThat(home.getProductCount()).isZero();
    }

    @Test
    @DisplayName("查询条件应透传给领域服务")
    void listShouldPassQueryConditions() {
        when(categoryDomainService.list("数码", 1)).thenReturn(Collections.emptyList());
        when(productDomainService.countGroupByCategory()).thenReturn(Collections.emptyMap());

        ListCategoryRequest request = new ListCategoryRequest();
        request.setName("数码");
        request.setStatus(1);

        List<CategoryDTO> tree = categoryApplicationService.list(request);

        assertThat(tree).isEmpty();
    }

    @Test
    @DisplayName("空类目列表应返回空树")
    void listShouldReturnEmptyTreeForNoCategories() {
        when(categoryDomainService.list(any(), any())).thenReturn(Collections.emptyList());
        when(productDomainService.countGroupByCategory()).thenReturn(Collections.emptyMap());

        assertThat(categoryApplicationService.list(new ListCategoryRequest())).isEmpty();
    }
}
