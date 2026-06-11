package com.awsome.shop.product.application.impl.service.product;

import com.awsome.shop.product.application.api.dto.product.ProductDTO;
import com.awsome.shop.product.application.api.dto.product.request.CreateProductRequest;
import com.awsome.shop.product.application.api.dto.product.request.ListProductRequest;
import com.awsome.shop.product.common.dto.PageResult;
import com.awsome.shop.product.domain.model.product.ProductEntity;
import com.awsome.shop.product.domain.service.product.ProductDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * ProductApplicationServiceImpl 单元测试
 */
@ExtendWith(MockitoExtension.class)
class ProductApplicationServiceImplTest {

    @Mock
    private ProductDomainService productDomainService;

    @InjectMocks
    private ProductApplicationServiceImpl productApplicationService;

    @Test
    @DisplayName("list 应转换 PageResult 并保留分页信息")
    void listShouldConvertPageResult() {
        ProductEntity entity = new ProductEntity();
        entity.setId(1L);
        entity.setName("商品A");
        entity.setSku("SKU-A");

        PageResult<ProductEntity> page = new PageResult<>();
        page.setCurrent(1L);
        page.setSize(20L);
        page.setTotal(1L);
        page.setPages(1L);
        page.setRecords(List.of(entity));

        when(productDomainService.page(1, 20, "商品", "数码")).thenReturn(page);

        ListProductRequest request = new ListProductRequest();
        request.setPage(1);
        request.setSize(20);
        request.setName("商品");
        request.setCategory("数码");

        PageResult<ProductDTO> result = productApplicationService.list(request);

        assertThat(result.getTotal()).isEqualTo(1L);
        assertThat(result.getCurrent()).isEqualTo(1L);
        assertThat(result.getRecords()).hasSize(1);
        assertThat(result.getRecords().get(0).getId()).isEqualTo(1L);
        assertThat(result.getRecords().get(0).getName()).isEqualTo("商品A");
        assertThat(result.getRecords().get(0).getSku()).isEqualTo("SKU-A");
    }

    @Test
    @DisplayName("create 应透传请求字段并完整映射 DTO")
    void createShouldDelegateAndMapAllFields() {
        List<Map<String, String>> specs = List.of(Map.of("颜色", "黑"));
        LocalDateTime now = LocalDateTime.of(2026, 6, 11, 10, 0);

        ProductEntity entity = new ProductEntity();
        entity.setId(5L);
        entity.setName("商品B");
        entity.setSku("SKU-B");
        entity.setCategory("家居");
        entity.setBrand("品牌");
        entity.setPointsPrice(800);
        entity.setMarketPrice(new BigDecimal("88.00"));
        entity.setStock(20);
        entity.setSoldCount(0);
        entity.setStatus(1);
        entity.setDescription("描述");
        entity.setImageUrl("img.png");
        entity.setSubtitle("副标题");
        entity.setDeliveryMethod("快递");
        entity.setServiceGuarantee("保障");
        entity.setPromotion("促销");
        entity.setColors("黑");
        entity.setSpecs(specs);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        when(productDomainService.create("商品B", "SKU-B", "家居", "品牌",
                800, new BigDecimal("88.00"), 20, 1, "描述", "img.png",
                "副标题", "快递", "保障", "促销", "黑", specs)).thenReturn(entity);

        CreateProductRequest request = new CreateProductRequest();
        request.setName("商品B");
        request.setSku("SKU-B");
        request.setCategory("家居");
        request.setBrand("品牌");
        request.setPointsPrice(800);
        request.setMarketPrice(new BigDecimal("88.00"));
        request.setStock(20);
        request.setStatus(1);
        request.setDescription("描述");
        request.setImageUrl("img.png");
        request.setSubtitle("副标题");
        request.setDeliveryMethod("快递");
        request.setServiceGuarantee("保障");
        request.setPromotion("促销");
        request.setColors("黑");
        request.setSpecs(specs);

        ProductDTO dto = productApplicationService.create(request);

        verify(productDomainService).create("商品B", "SKU-B", "家居", "品牌",
                800, new BigDecimal("88.00"), 20, 1, "描述", "img.png",
                "副标题", "快递", "保障", "促销", "黑", specs);

        assertThat(dto.getId()).isEqualTo(5L);
        assertThat(dto.getName()).isEqualTo("商品B");
        assertThat(dto.getSku()).isEqualTo("SKU-B");
        assertThat(dto.getCategory()).isEqualTo("家居");
        assertThat(dto.getBrand()).isEqualTo("品牌");
        assertThat(dto.getPointsPrice()).isEqualTo(800);
        assertThat(dto.getMarketPrice()).isEqualByComparingTo("88.00");
        assertThat(dto.getStock()).isEqualTo(20);
        assertThat(dto.getSoldCount()).isZero();
        assertThat(dto.getStatus()).isEqualTo(1);
        assertThat(dto.getDescription()).isEqualTo("描述");
        assertThat(dto.getImageUrl()).isEqualTo("img.png");
        assertThat(dto.getSubtitle()).isEqualTo("副标题");
        assertThat(dto.getDeliveryMethod()).isEqualTo("快递");
        assertThat(dto.getServiceGuarantee()).isEqualTo("保障");
        assertThat(dto.getPromotion()).isEqualTo("促销");
        assertThat(dto.getColors()).isEqualTo("黑");
        assertThat(dto.getSpecs()).isEqualTo(specs);
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }
}
