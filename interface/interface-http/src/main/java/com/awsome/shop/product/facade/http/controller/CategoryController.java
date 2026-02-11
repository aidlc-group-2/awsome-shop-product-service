package com.awsome.shop.product.facade.http.controller;

import com.awsome.shop.product.application.api.dto.category.CategoryDTO;
import com.awsome.shop.product.application.api.dto.category.request.ListCategoryRequest;
import com.awsome.shop.product.application.api.service.category.CategoryApplicationService;
import com.awsome.shop.product.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类目管理 Controller
 */
@Tag(name = "Category", description = "类目管理")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryApplicationService categoryApplicationService;

    @Operation(summary = "查询类目列表（树形结构）")
    @PostMapping("/public/category/list")
    public Result<List<CategoryDTO>> list(@RequestBody @Valid ListCategoryRequest request) {
        return Result.success(categoryApplicationService.list(request));
    }
}
