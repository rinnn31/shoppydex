package com.github.rinnn31.shoppydex.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.rinnn31.shoppydex.model.api.ApiResponse;
import com.github.rinnn31.shoppydex.model.api.ProductModel;
import com.github.rinnn31.shoppydex.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {
    @Autowired
    private ProductService productService;
    
    @PostMapping()
    public ApiResponse<?> addProduct(@Valid @RequestBody ProductModel productModel) {
        productService.addProduct(productModel);
        return ApiResponse.success(null);
    }
    @GetMapping("/categories")
    public ApiResponse<?> getListCategories() {
        List<String> categories = productService.getAllCategories();
        return ApiResponse.success(categories);
    }

    @GetMapping
    public ApiResponse<?> getProducts(@RequestParam(name = "category", required = false) String category) {
        List<ProductModel> product = productService.getProducts(category);
        return ApiResponse.success(product);
    }

    @PatchMapping
    public ApiResponse<?> updateProduct(@RequestBody ProductModel productModel) {
        productService.updateProduct(productModel);
        return ApiResponse.success(null);
    }

    @DeleteMapping
    public ApiResponse<?> deleteProduct(@RequestParam("productId") Long productId) {
        productService.deleteProduct(productId);
        return ApiResponse.success(null);
    }
}

