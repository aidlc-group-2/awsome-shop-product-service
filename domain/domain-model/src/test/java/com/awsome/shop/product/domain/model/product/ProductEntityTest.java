package com.awsome.shop.product.domain.model.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ProductEntity 单元测试
 */
class ProductEntityTest {

    @Test
    @DisplayName("updateInfo 应更新全部业务字段")
    void updateInfoShouldUpdateAllFields() {
        ProductEntity entity = new ProductEntity();
        entity.setId(1L);
        entity.setName("旧名称");
        entity.setSku("OLD-SKU");
        entity.setSoldCount(5);

        List<Map<String, String>> specs = List.of(Map.of("颜色", "黑色"));
        entity.updateInfo("新名称", "NEW-SKU", "数码", "TestBrand",
                1000, new BigDecimal("99.99"), 50,
                1, "描述", "http://img.example.com/1.png",
                "副标题", "顺丰包邮", "七天无理由",
                "限时促销", "黑,白", specs);

        assertThat(entity.getName()).isEqualTo("新名称");
        assertThat(entity.getSku()).isEqualTo("NEW-SKU");
        assertThat(entity.getCategory()).isEqualTo("数码");
        assertThat(entity.getBrand()).isEqualTo("TestBrand");
        assertThat(entity.getPointsPrice()).isEqualTo(1000);
        assertThat(entity.getMarketPrice()).isEqualByComparingTo("99.99");
        assertThat(entity.getStock()).isEqualTo(50);
        assertThat(entity.getStatus()).isEqualTo(1);
        assertThat(entity.getDescription()).isEqualTo("描述");
        assertThat(entity.getImageUrl()).isEqualTo("http://img.example.com/1.png");
        assertThat(entity.getSubtitle()).isEqualTo("副标题");
        assertThat(entity.getDeliveryMethod()).isEqualTo("顺丰包邮");
        assertThat(entity.getServiceGuarantee()).isEqualTo("七天无理由");
        assertThat(entity.getPromotion()).isEqualTo("限时促销");
        assertThat(entity.getColors()).isEqualTo("黑,白");
        assertThat(entity.getSpecs()).isEqualTo(specs);
    }

    @Test
    @DisplayName("updateInfo 不应修改 id 和 soldCount")
    void updateInfoShouldNotTouchIdAndSoldCount() {
        ProductEntity entity = new ProductEntity();
        entity.setId(9L);
        entity.setSoldCount(123);

        entity.updateInfo("名称", "SKU-1", "分类", null,
                10, null, null, null, null, null,
                null, null, null, null, null, null);

        assertThat(entity.getId()).isEqualTo(9L);
        assertThat(entity.getSoldCount()).isEqualTo(123);
        assertThat(entity.getStock()).isNull();
        assertThat(entity.getStatus()).isNull();
    }
}
