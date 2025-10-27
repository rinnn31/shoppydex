package com.github.rinnn31.shoppydex.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.rinnn31.shoppydex.model.api.ApiResponse;
import com.github.rinnn31.shoppydex.model.api.CategoryDTO;
import com.github.rinnn31.shoppydex.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryApiController {
    @Autowired
    private ProductService categoryService;
    
    @PostMapping("/add")
    public ApiResponse<?> addCategorywithImage(@Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.addCategory(categoryDTO);
        return ApiResponse.success(null);
    }
    @GetMapping("/type")
    public ApiResponse<?> getCategoryTypes() {
        List<String> types = categoryService.getAllCategoryTypes();
        return ApiResponse.success(types);
    }
    @GetMapping
    public ApiResponse<?> getCategoriesByType(@RequestParam(name = "type", required = false) String type) {
        List<CategoryDTO> categories = categoryService.getCategoryDTOByType(type);
        return ApiResponse.success(categories);
    }
}

