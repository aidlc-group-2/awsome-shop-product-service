package com.awsome.shop.product.repository.mysql.po.stock;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 库存预占记录持久化对象
 */
@Data
@TableName("stock_reservation")
public class StockReservationPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String reservationId;

    private String orderRef;

    private Long productId;

    private Integer quantity;

    private String status;

    @TableField(fill = com.baomidou.mybatisplus.annotation.FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = com.baomidou.mybatisplus.annotation.FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
