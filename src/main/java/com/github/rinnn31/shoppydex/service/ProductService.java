package com.github.rinnn31.shoppydex.service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.rinnn31.shoppydex.exception.SPDException;
import com.github.rinnn31.shoppydex.model.Category;
import com.github.rinnn31.shoppydex.model.Product;
import com.github.rinnn31.shoppydex.model.api.ApiResponse;
import com.github.rinnn31.shoppydex.model.api.CategoryDTO;
import com.github.rinnn31.shoppydex.repository.CategoryRepository;
import com.github.rinnn31.shoppydex.repository.ProductRepository;
import com.github.rinnn31.shoppydex.utils.WebImageResolver;




@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    public void deleteProductsByCategory(Long categoryId){
        if (!categoryRepository.existsById(categoryId)) {
            throw new SPDException(101, "Danh muc khong ton tai");
        } 
        
        productRepository.deleteByCategoryId(categoryId);
        categoryRepository.updateStock(categoryId,0);

    }
    public void deleteCategory(Long categoryId){
        categoryRepository.deleteById(categoryId);
        productRepository.deleteByCategoryId(categoryId);
    }
    public void deleteProduct(Long productId){
        Optional<Product> product = productRepository.findById(productId);
        if(product.isEmpty()){
            throw new SPDException(101, "Khong ton tai san pham");
        }
        if(product.get().getCategory().getProductType().equals("MULTI_ITEM")){
            throw new SPDException(102, "Khong the xoa san pham MULTI_ITEM");
        }
        // if("MULTI_ITEM".equals(product.get().getCategory().getProductType()));
        
        productRepository.delete(product.get());
        Category category = product.get().getCategory();
        category.setStock(category.getStock()-1);
        categoryRepository.save(category);
    }
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

    // Cập nhật lại số lượng tồn của danh mục sau khi xóa sản phẩm
    
    public void deleteCategoryById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public void deleteProductById(Long productId) {
        productRepository.deleteById(productId);
    }

    
    public ProductService() {
    }

    // THÊM DANH MỤC MỚI
    public boolean isCategoryNameTaken(String name) {
        return categoryRepository.existsByName(name);
    }

    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    } 
    

    // Xử lý điều kiện trùng tên danh mục-> Trả về mã lỗi 101:
    // Tạo ngoại lệ nếu trùng tên danh mục thì ngăn chặn việc thêm mới danh mục và trả về thông báo lỗi phù hợp
    //  "Tên danh mục đã tồn tại" và mã lỗi 101
    public void addCategory(CategoryDTO categoryDTO) {
        // kiểm tra trùng tên danh mục
        if (existsByName(categoryDTO.getName())) {
            throw new SPDException(101, "Tên danh mục đã tồn tại!");
        }


        //Chuyển đổi categoryImage từ chuỗi base64 sang dạng byte[]
       String imagePath = null;
        if (categoryDTO.getCategoryImage() != null && !categoryDTO.getCategoryImage().isBlank()) {
            imagePath = WebImageResolver.pushImage(
                categoryDTO.getName() + "-" + UUID.randomUUID() + ".png",
                categoryDTO.getCategoryImage()
            );


        }

        // Thằng repository cần dùng entity, nên đổi từ categoryDTO sang Category
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setType(categoryDTO.getType());
        category.setProductType(categoryDTO.getProductType());
        category.setPrice(categoryDTO.getPrice());
        category.setStock(categoryDTO.getStock());
        category.setCategoryImage(imagePath);
        categoryRepository.save(category);
    }

    // LẤY DANH SÁCH LOẠI DANH MỤC THEO LOẠI (nếu type= null thì trả về danh sách trống)
    public List<CategoryDTO> getCategoryDTOByType(String type) {
        if( !StringUtils.hasText(type)){
            return categoryRepository.findAll().stream().map(CategoryDTO::new).collect(Collectors.toList());
        }
        else{
            List<Category> categoriesByType = categoryRepository.findByType(type);
            return categoriesByType.stream().map(CategoryDTO::new).collect(Collectors.toList());// return danh sách CategoryDTO được lọc theo type
        }   
    }

    // LẤY DANH SÁCH LOẠI DANH MỤC
    public List<String> getAllCategoryTypes() {
        return categoryRepository.findDistinctByType();
    }



} 
