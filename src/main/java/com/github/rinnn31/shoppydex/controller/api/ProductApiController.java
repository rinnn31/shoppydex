package com.github.rinnn31.shoppydex.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.rinnn31.shoppydex.model.dto.ApiResponse;
import com.github.rinnn31.shoppydex.model.dto.ProductModel;
import com.github.rinnn31.shoppydex.model.dto.ProductsPageModel;
import com.github.rinnn31.shoppydex.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {
    @Autowired
    private ProductService productService;
    
    @PostMapping
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
    public ApiResponse<?> getProducts(@RequestParam(name = "categories", required = false) String categoriesString,
                                      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                      @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        List<String> categories = null;
        if (categoriesString != null && !categoriesString.isBlank()) {
            categories = List.of(categoriesString.split(","));
        }   
        ProductsPageModel productsPage = productService.getProducts(categories, page, size);
        return ApiResponse.success(productsPage);
    }

    @PatchMapping
    public ApiResponse<?> updateProduct(@RequestBody ProductModel productModel) {
        productService.updateProduct(productModel);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<?> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ApiResponse.success(null);
    }

    @GetMapping("/{productId}")
    public ApiResponse<?> getProduct(@PathVariable Long productId) {
        ProductModel product = productService.getProduct(productId);
        return ApiResponse.success(product);
    }

    @PostMapping("/{productId}/items")
    public ApiResponse<?> addProductItems(@PathVariable Long productId,
                                         @RequestBody List<String> items) {
        productService.addItemsToProduct(productId, items);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{productId}/items")
    public ApiResponse<?> deleteProductItems(@PathVariable Long productId) {
        productService.clearItemsFromProduct(productId);
        return ApiResponse.success(null);
    }
}

