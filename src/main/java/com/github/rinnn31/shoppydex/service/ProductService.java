package com.github.rinnn31.shoppydex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.rinnn31.shoppydex.repository.CategoryRepository;
import com.github.rinnn31.shoppydex.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public boolean categoryExisted(Long categoryId) {
        return categoryRepository.existsById(categoryId);
    }

    public boolean productExisted(Long productId) {
        return productRepository.existsById(productId);
    }

    public boolean isMultiItemCategory(Long productId) {
        // Ví dụ: giả sử Product có trường "category.type"
        return productRepository.findById(productId)
                .map(product -> product.getCategory().getType().equals("MULTI_ITEM"))
                .orElse(false);
    }

    public void deleteProductsByCategory(Long categoryId) {
        productRepository.deleteByCategoryId(categoryId);
    }

    // Cập nhật lại số lượng tồn của danh mục sau khi xóa sản phẩm
    public void updateCategoryStock(Long categoryId) {
        // Ví dụ: đếm lại tổng tồn kho sau khi xóa
        int totalStock = productRepository.sumStockByCategoryId(categoryId);
        categoryRepository.updateStock(categoryId, totalStock);
    }
    
    public void deleteCategoryById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public void deleteProductById(Long productId) {
        productRepository.deleteById(productId);
    }

    public void updateCategoryStockByProductId(Long productId) {
        Long categoryId = productRepository.findCategoryIdByProductId(productId);
        updateCategoryStock(categoryId);
    }
}
