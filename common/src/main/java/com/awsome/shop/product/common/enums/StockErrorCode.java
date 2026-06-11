package com.awsome.shop.product.common.enums;

/**
 * 库存相关业务错误码
 *
 * <p>错误码前缀决定 HTTP 状态码映射，参见 {@link ErrorCode} 接口说明。</p>
 */
public enum StockErrorCode implements ErrorCode {

    /**
     * 商品不存在
     */
    PRODUCT_NOT_FOUND("NOT_FOUND_010", "商品不存在: {0}"),

    /**
     * 库存不足
     */
    INSUFFICIENT_STOCK("CONFLICT_010", "商品库存不足: productId={0}, 需要={1}"),

    /**
     * 预占记录不存在
     */
    RESERVATION_NOT_FOUND("NOT_FOUND_011", "库存预占记录不存在: {0}"),

    /**
     * 预占状态非法（无法对已扣减/已释放的预占再次操作）
     */
    RESERVATION_STATE_INVALID("CONFLICT_011", "库存预占状态非法: reservationId={0}, status={1}"),

    /**
     * 预占数量非法
     */
    INVALID_QUANTITY("PARAM_010", "预占数量必须大于 0");

    private final String code;
    private final String message;

    StockErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
