CREATE TABLE `stock_reservation` (
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `reservation_id` VARCHAR(64)  NOT NULL COMMENT '预占凭据(对外幂等句柄)',
    `order_ref`      VARCHAR(64)  NOT NULL COMMENT '关联订单号(下单幂等键)',
    `product_id`     BIGINT       NOT NULL COMMENT '商品ID',
    `quantity`       INT          NOT NULL COMMENT '预占数量',
    `status`         VARCHAR(16)  NOT NULL COMMENT '状态 RESERVED-已预占 CONFIRMED-已扣减 RELEASED-已释放',
    `created_at`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_reservation_id` (`reservation_id`),
    UNIQUE INDEX `uk_order_ref_product` (`order_ref`, `product_id`),
    INDEX `idx_product_id` (`product_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '库存预占记录表';
