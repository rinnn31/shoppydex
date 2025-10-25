package com.github.rinnn31.shoppydex.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.rinnn31.shoppydex.model.api.ApiResponse;
import com.github.rinnn31.shoppydex.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    @Autowired
    private ProductService productService;

    // XÓA TẤT CẢ SẢN PHẨM TRONG DANH MỤC
    @DeleteMapping
    public ApiResponse<Void> deleteProductsByCategory(@RequestParam("categoryId") Long categoryId) {

        if (!productService.categoryExisted(categoryId)) {
            return ApiResponse.error(101, "Danh mục không tồn tại!");
        }

        productService.deleteProductsByCategory(categoryId);
        productService.updateCategoryStock(categoryId);

        return ApiResponse.success(null);
    }

    // XÓA DANH MỤC
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {

        if (!productService.categoryExisted(id)) {
            return ApiResponse.error(101, "Danh mục không tồn tại!");
        }

        productService.deleteProductsByCategory(id);
        productService.deleteCategoryById(id);

        return ApiResponse.success(null);
    }

    @DeleteMapping("/item/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable Long id) {

        if (!productService.productExisted(id)) {
            
            return ApiResponse.error(101, "Id sản phẩm không tồn tại!");
        }

        if (productService.isMultiItemCategory(id)) {
            return ApiResponse.error(102, "Không thể xóa sản phẩm MULTI_ITEM!");
        }

        productService.deleteProductById(id);
        productService.updateCategoryStockByProductId(id);

        return ApiResponse.success(null);
    }
}
