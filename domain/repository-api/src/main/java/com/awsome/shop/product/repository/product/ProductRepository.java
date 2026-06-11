package com.awsome.shop.product.repository.product;

import com.awsome.shop.product.common.dto.PageResult;
import com.awsome.shop.product.domain.model.product.ProductEntity;

import java.util.Map;

/**
 * Product 仓储接口
 */
public interface ProductRepository {

    ProductEntity getById(Long id);

    ProductEntity getBySku(String sku);

    PageResult<ProductEntity> page(int page, int size, String name, String category);

    void save(ProductEntity entity);

    void update(ProductEntity entity);

    void deleteById(Long id);

    /**
     * 条件扣减库存（防超卖）。
     *
     * @return true 表示扣减成功，false 表示库存不足或商品不存在
     */
    boolean deductStock(Long productId, int quantity);

    /**
     * 回补库存（释放预占 / Saga 补偿）。
     *
     * @return true 表示回补成功
     */
    boolean restoreStock(Long productId, int quantity);

    /**
     * 累加已兑换数量（发货确认）。
     */
    void increaseSoldCount(Long productId, int quantity);

    /**
     * 按分类名称统计商品数量
     *
     * @return Map，key 为分类名称，value 为商品数量
     */
    Map<String, Long> countGroupByCategory();
}
