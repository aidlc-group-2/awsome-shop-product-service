package com.awsome.shop.product.common.exception;

import com.awsome.shop.product.common.enums.SampleErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * BusinessException 单元测试
 */
class BusinessExceptionTest {

    @Test
    @DisplayName("使用 ErrorCode 枚举时应携带默认消息")
    void errorCodeConstructorShouldUseDefaultMessage() {
        BusinessException ex = new BusinessException(SampleErrorCode.RESOURCE_NOT_FOUND);

        assertThat(ex.getErrorCode()).isEqualTo("NOT_FOUND_001");
        assertThat(ex.getErrorMessage()).isEqualTo("资源不存在");
        assertThat(ex.getMessage()).isEqualTo("资源不存在");
    }

    @Test
    @DisplayName("自定义消息应覆盖默认消息")
    void customMessageShouldOverrideDefault() {
        BusinessException ex = new BusinessException(SampleErrorCode.RESOURCE_NOT_FOUND, "商品不存在");

        assertThat(ex.getErrorCode()).isEqualTo("NOT_FOUND_001");
        assertThat(ex.getErrorMessage()).isEqualTo("商品不存在");
    }

    @Test
    @DisplayName("参数化消息应按 MessageFormat 格式化")
    void parameterizedMessageShouldBeFormatted() {
        BusinessException ex = new BusinessException(
                SampleErrorCode.RESOURCE_ALREADY_EXISTS, (Object) "order-123");

        assertThat(ex.getErrorCode()).isEqualTo("CONFLICT_001");
        assertThat(ex.getErrorMessage()).isEqualTo("资源已存在: order-123");
    }

    @Test
    @DisplayName("ErrorCode + 原因应保留 cause")
    void errorCodeWithCauseShouldPreserveCause() {
        IllegalStateException cause = new IllegalStateException("db down");
        BusinessException ex = new BusinessException(SampleErrorCode.OPERATION_NOT_ALLOWED, cause);

        assertThat(ex.getErrorCode()).isEqualTo("AUTHZ_001");
        assertThat(ex.getCause()).isSameAs(cause);
    }

    @Test
    @DisplayName("传统方式 String 错误码 + 消息")
    void legacyStringConstructorShouldWork() {
        BusinessException ex = new BusinessException("AUTH_001", "用户名或密码错误");

        assertThat(ex.getErrorCode()).isEqualTo("AUTH_001");
        assertThat(ex.getErrorMessage()).isEqualTo("用户名或密码错误");
    }
}
