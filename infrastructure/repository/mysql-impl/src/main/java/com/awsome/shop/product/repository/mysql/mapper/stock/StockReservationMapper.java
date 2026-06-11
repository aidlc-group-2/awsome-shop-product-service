package com.awsome.shop.product.repository.mysql.mapper.stock;

import com.awsome.shop.product.repository.mysql.po.stock.StockReservationPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存预占记录 Mapper
 */
@Mapper
public interface StockReservationMapper extends BaseMapper<StockReservationPO> {
}
