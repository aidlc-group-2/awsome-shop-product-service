package com.awsome.shop.product.facade.http.controller;

import com.awsome.shop.product.application.api.dto.product.ProductDTO;
import com.awsome.shop.product.application.api.dto.product.ProductSnapshotDTO;
import com.awsome.shop.product.application.api.dto.product.request.CreateProductRequest;
import com.awsome.shop.product.application.api.dto.product.request.ListProductRequest;
import com.awsome.shop.product.application.api.service.product.ProductApplicationService;
import com.awsome.shop.product.common.dto.PageResult;
import com.awsome.shop.product.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品管理 Controller
 */
@Tag(name = "Product", description = "商品管理")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {

    private final ProductApplicationService productApplicationService;

    @Operation(summary = "创建商品")
    @PostMapping("/public/product/create")
    public Result<ProductDTO> create(@RequestBody @Valid CreateProductRequest request) {
        return Result.success(productApplicationService.create(request));
    }

    @Operation(summary = "商品列表查询")
    @PostMapping("/public/product/list")
    public Result<PageResult<ProductDTO>> list(@RequestBody @Valid ListProductRequest request) {
        return Result.success(productApplicationService.list(request));
    }

    @Operation(summary = "商品权威快照（内部接口，供下单核价）")
    @GetMapping("/private/product/{id}/snapshot")
    public Result<ProductSnapshotDTO> getSnapshot(@PathVariable("id") Long id) {
        return Result.success(productApplicationService.getSnapshot(id));
    }
}
