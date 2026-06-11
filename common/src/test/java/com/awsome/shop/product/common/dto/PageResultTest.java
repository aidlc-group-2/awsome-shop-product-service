package com.awsome.shop.product.common.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * PageResult 单元测试
 */
class PageResultTest {

    @Test
    @DisplayName("convert 应转换记录类型并保留分页字段")
    void convertShouldTransformRecordsAndKeepPaginationFields() {
        PageResult<Integer> source = new PageResult<>();
        source.setCurrent(2L);
        source.setSize(10L);
        source.setTotal(35L);
        source.setPages(4L);
        source.setRecords(List.of(1, 2, 3));

        PageResult<String> converted = source.convert(i -> "item-" + i);

        assertThat(converted.getCurrent()).isEqualTo(2L);
        assertThat(converted.getSize()).isEqualTo(10L);
        assertThat(converted.getTotal()).isEqualTo(35L);
        assertThat(converted.getPages()).isEqualTo(4L);
        assertThat(converted.getRecords()).containsExactly("item-1", "item-2", "item-3");
    }

    @Test
    @DisplayName("convert 空记录列表应返回空列表")
    void convertShouldHandleEmptyRecords() {
        PageResult<Integer> source = new PageResult<>();
        source.setCurrent(1L);
        source.setSize(10L);
        source.setTotal(0L);
        source.setPages(0L);
        source.setRecords(Collections.emptyList());

        PageResult<String> converted = source.convert(String::valueOf);

        assertThat(converted.getRecords()).isEmpty();
        assertThat(converted.getTotal()).isZero();
    }

    @Test
    @DisplayName("convert 返回新实例，不影响原对象")
    void convertShouldReturnNewInstance() {
        PageResult<Integer> source = new PageResult<>();
        source.setCurrent(1L);
        source.setSize(5L);
        source.setTotal(1L);
        source.setPages(1L);
        source.setRecords(List.of(42));

        PageResult<String> converted = source.convert(String::valueOf);

        assertThat(converted).isNotSameAs(source);
        assertThat(source.getRecords()).containsExactly(42);
    }
}
