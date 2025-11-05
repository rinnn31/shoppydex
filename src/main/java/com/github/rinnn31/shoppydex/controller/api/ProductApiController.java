package com.github.rinnn31.shoppydex.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.rinnn31.shoppydex.model.api.ApiResponse;
import com.github.rinnn31.shoppydex.model.api.ProductDTO;
import com.github.rinnn31.shoppydex.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    @Autowired
    private ProductService productService;

    // XÓA TẤT CẢ SẢN PHẨM TRONG DANH MỤC
    @DeleteMapping
    public ApiResponse<Void> deleteProductsByCategory(@RequestParam("categoryId") Long categoryId) {

        productService.deleteProductsByCategory(categoryId);
        return ApiResponse.success(null);
    }

    // XÓA DANH MỤC
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
        productService.deleteCategory(id);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/item/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ApiResponse.success(null);
    }

    @PostMapping
    public ApiResponse<Void> addProduct(@RequestBody ProductDTO productDTO) {
        productService.addProduct(productDTO);
        return ApiResponse.success(null);
    }

    @GetMapping
    public ApiResponse<List<ProductDTO>> getProductsByCategory(@RequestParam("categoryId") Long categoryId) {
        return ApiResponse.success(productService.getProductsByCategoryId(categoryId));
    }

}
