package com.awsome.shop.product.repository.mysql.mapper.product;

import com.awsome.shop.product.repository.mysql.po.product.ProductPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * Product Mapper 接口
 */
@Mapper
public interface ProductMapper extends BaseMapper<ProductPO> {

    /**
     * 分页查询
     *
     * @param page     MyBatis-Plus 分页对象
     * @param name     名称模糊查询条件（可为 null）
     * @param category 分类精确筛选条件（可为 null）
     * @return 分页结果
     */
    IPage<ProductPO> selectPage(IPage<ProductPO> page, @Param("name") String name, @Param("category") String category);

    /**
     * 按分类名称统计商品数量
     *
     * @return Map，key 为分类名称，value 为商品数量
     */
    @MapKey("category")
    Map<String, Map<String, Object>> countGroupByCategory();

    /**
     * 条件扣减库存（防超卖）。仅当库存充足时才扣减成功。
     *
     * @param productId 商品ID
     * @param quantity  扣减数量
     * @return 受影响行数（0 表示库存不足或商品不存在，扣减失败）
     */
    int deductStock(@Param("productId") Long productId, @Param("quantity") int quantity);

    /**
     * 回补库存（释放预占或 Saga 补偿）。
     *
     * @param productId 商品ID
     * @param quantity  回补数量
     * @return 受影响行数
     */
    int restoreStock(@Param("productId") Long productId, @Param("quantity") int quantity);

    /**
     * 累加已兑换数量（发货确认时）。
     *
     * @param productId 商品ID
     * @param quantity  数量
     * @return 受影响行数
     */
    int increaseSoldCount(@Param("productId") Long productId, @Param("quantity") int quantity);
}
