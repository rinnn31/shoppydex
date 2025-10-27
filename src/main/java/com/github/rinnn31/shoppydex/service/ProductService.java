package com.github.rinnn31.shoppydex.service;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.rinnn31.shoppydex.exception.SPDException;
import com.github.rinnn31.shoppydex.model.Category;
import com.github.rinnn31.shoppydex.model.api.CategoryDTO;
import com.github.rinnn31.shoppydex.repository.CategoryRepository;
import com.github.rinnn31.shoppydex.repository.ProductRepository;
import com.github.rinnn31.shoppydex.utils.WebImageResolver;




@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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
            WebImageResolver.writeImage(
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