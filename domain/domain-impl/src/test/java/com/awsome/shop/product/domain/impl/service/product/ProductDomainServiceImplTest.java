package com.awsome.shop.product.domain.impl.service.product;

import com.awsome.shop.product.common.dto.PageResult;
import com.awsome.shop.product.common.exception.BusinessException;
import com.awsome.shop.product.domain.model.product.ProductEntity;
import com.awsome.shop.product.repository.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * ProductDomainServiceImpl 单元测试
 */
@ExtendWith(MockitoExtension.class)
class ProductDomainServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductDomainServiceImpl productDomainService;

    @Test
    @DisplayName("getById 存在时返回实体")
    void getByIdShouldReturnEntityWhenFound() {
        ProductEntity entity = new ProductEntity();
        entity.setId(1L);
        when(productRepository.getById(1L)).thenReturn(entity);

        ProductEntity result = productDomainService.getById(1L);

        assertThat(result).isSameAs(entity);
    }

    @Test
    @DisplayName("getById 不存在时抛 BusinessException(NOT_FOUND_001)")
    void getByIdShouldThrowWhenNotFound() {
        when(productRepository.getById(99L)).thenReturn(null);

        assertThatThrownBy(() -> productDomainService.getById(99L))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo("NOT_FOUND_001");
    }

    @Test
    @DisplayName("page 应原样透传分页参数")
    void pageShouldDelegateToRepository() {
        PageResult<ProductEntity> pageResult = new PageResult<>();
        when(productRepository.page(2, 10, "手机", "数码")).thenReturn(pageResult);

        PageResult<ProductEntity> result = productDomainService.page(2, 10, "手机", "数码");

        assertThat(result).isSameAs(pageResult);
        verify(productRepository).page(2, 10, "手机", "数码");
    }

    @Test
    @DisplayName("create 成功时保存并回查返回")
    void createShouldSaveAndReturnReloadedEntity() {
        when(productRepository.getBySku("SKU-1")).thenReturn(null);
        // save 时模拟数据库回填 id
        doAnswer(invocation -> {
            ProductEntity e = invocation.getArgument(0);
            e.setId(100L);
            return null;
        }).when(productRepository).save(any(ProductEntity.class));
        ProductEntity reloaded = new ProductEntity();
        reloaded.setId(100L);
        when(productRepository.getById(100L)).thenReturn(reloaded);

        List<Map<String, String>> specs = List.of(Map.of("尺寸", "L"));
        ProductEntity result = productDomainService.create("商品", "SKU-1", "数码", "品牌",
                500, new BigDecimal("9.99"), 10,
                1, "描述", "img.png",
                "副标题", "快递", "保障",
                "促销", "红", specs);

        assertThat(result).isSameAs(reloaded);

        ArgumentCaptor<ProductEntity> captor = ArgumentCaptor.forClass(ProductEntity.class);
        verify(productRepository).save(captor.capture());
        ProductEntity saved = captor.getValue();
        assertThat(saved.getName()).isEqualTo("商品");
        assertThat(saved.getSku()).isEqualTo("SKU-1");
        assertThat(saved.getStock()).isEqualTo(10);
        assertThat(saved.getStatus()).isEqualTo(1);
        assertThat(saved.getSpecs()).isEqualTo(specs);
    }

    @Test
    @DisplayName("create 时 stock/status 为 null 应默认 0")
    void createShouldDefaultStockAndStatusToZero() {
        when(productRepository.getBySku("SKU-2")).thenReturn(null);
        when(productRepository.getById(any())).thenReturn(new ProductEntity());

        productDomainService.create("商品", "SKU-2", "数码", null,
                100, null, null,
                null, null, null,
                null, null, null,
                null, null, null);

        ArgumentCaptor<ProductEntity> captor = ArgumentCaptor.forClass(ProductEntity.class);
        verify(productRepository).save(captor.capture());
        assertThat(captor.getValue().getStock()).isZero();
        assertThat(captor.getValue().getStatus()).isZero();
    }

    @Test
    @DisplayName("create 时 SKU 已存在应抛 BusinessException(CONFLICT_001) 且不保存")
    void createShouldThrowWhenSkuExists() {
        when(productRepository.getBySku("DUP-SKU")).thenReturn(new ProductEntity());

        assertThatThrownBy(() -> productDomainService.create("商品", "DUP-SKU", "数码", null,
                100, null, null, null, null, null,
                null, null, null, null, null, null))
                .isInstanceOf(BusinessException.class)
                .satisfies(e -> {
                    BusinessException be = (BusinessException) e;
                    assertThat(be.getErrorCode()).isEqualTo("CONFLICT_001");
                    assertThat(be.getErrorMessage()).contains("DUP-SKU");
                });

        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("countGroupByCategory 应透传仓储结果")
    void countGroupByCategoryShouldDelegate() {
        Map<String, Long> counts = Map.of("数码", 3L, "家居", 1L);
        when(productRepository.countGroupByCategory()).thenReturn(counts);

        assertThat(productDomainService.countGroupByCategory()).isEqualTo(counts);
    }
}
