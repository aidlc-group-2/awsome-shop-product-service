package com.awsome.shop.product.application.api.dto.product;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品权威快照（供 order-service 下单核价，FR-O1）
 *
 * <p>仅暴露下单决策所需的权威字段：积分价、类型、上下架状态、库存。
 * order-service 必须以此校验客户端传入的金额/类型，杜绝伪造低价兑换。</p>
 */
@Data
public class ProductSnapshotDTO {

    private Long id;

    private String name;

    /** 权威积分单价 */
    private Integer pointsPrice;

    private BigDecimal marketPrice;

    /** 状态 0-已下架 1-已上架 */
    private Integer status;

    /** 当前库存 */
    private Integer stock;
}
