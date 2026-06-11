package com.awsome.shop.product.domain.model.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CategoryEntity 单元测试
 */
class CategoryEntityTest {

    @Test
    @DisplayName("updateInfo 应更新全部业务字段")
    void updateInfoShouldUpdateAllFields() {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(1L);
        entity.setName("旧分类");

        entity.updateInfo("新分类", 2L, "icon.png", 10, 1, "分类描述");

        assertThat(entity.getName()).isEqualTo("新分类");
        assertThat(entity.getParentId()).isEqualTo(2L);
        assertThat(entity.getIcon()).isEqualTo("icon.png");
        assertThat(entity.getSortOrder()).isEqualTo(10);
        assertThat(entity.getStatus()).isEqualTo(1);
        assertThat(entity.getDescription()).isEqualTo("分类描述");
    }

    @Test
    @DisplayName("updateInfo 不应修改 id，允许置空 parentId")
    void updateInfoShouldKeepIdAndAllowNullParent() {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(7L);
        entity.setParentId(3L);

        entity.updateInfo("一级分类", null, null, 0, 0, null);

        assertThat(entity.getId()).isEqualTo(7L);
        assertThat(entity.getParentId()).isNull();
        assertThat(entity.getIcon()).isNull();
        assertThat(entity.getDescription()).isNull();
    }
}
