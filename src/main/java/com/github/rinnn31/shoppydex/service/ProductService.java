package com.github.rinnn31.shoppydex.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.rinnn31.shoppydex.model.Category;
import com.github.rinnn31.shoppydex.model.api.CategoryDTO;
import com.github.rinnn31.shoppydex.repository.CategoryRepository;
import com.github.rinnn31.shoppydex.repository.ProductRepository;

import jakarta.annotation.PostConstruct;



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
    
    @Value("${images.upload-dir:./images}")
    private String uploadDir;
    @PostConstruct
    public void makeSureUploadDirExists(){
        try{
            Files.createDirectories(Paths.get(uploadDir));
        }
        catch (IOException e){
            throw new RuntimeException("Không thể tạo thư mục lưu trữ hình ảnh: " + uploadDir, e);
        }
    }
    private String saveImageFile(byte[] imageData, String categoryName) {
        if (imageData == null || imageData.length == 0) {
            return null;
        }
        // tạo ra tên file an toàn
        String safe = categoryName == null ? "SafeFile" : categoryName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
        String fileName = safe + "-" + UUID.randomUUID() + ".png";
        Path filePath = Paths.get(uploadDir, fileName);
        try {
            Files.write(filePath, imageData);
            return "/images/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu tệp hình ảnh: " + fileName, e);
        }
    }
    // Xử lý điều kiện trùng tên danh mục-> Trả về mã lỗi 101:
    // Tạo ngoại lệ nếu trùng tên danh mục thì ngăn chặn việc thêm mới danh mục và trả về thông báo lỗi phù hợp
    //  "Tên danh mục đã tồn tại" và mã lỗi 101
    public class DuplicateResourceException extends RuntimeException {
        public DuplicateResourceException(String message) {
            super(message);
        }
    }
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        // kiểm tra trùng tên danh mục
        if (existsByName(categoryDTO.getName())) {
            throw new DuplicateResourceException("Tên danh mục đã tồn tại!");
        }
        //Chuyển đổi categoryImage từ chuỗi base64 sang dạng byte[]
       String imagePath = null;
        if (categoryDTO.getCategoryImage() != null && !categoryDTO.getCategoryImage().isBlank()) {
            byte[] imageData = decodeCatogoryImage(categoryDTO.getCategoryImage());
            imagePath = saveImageFile(imageData, categoryDTO.getName());
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
        Category saved  = categoryRepository.save(category);

        return new CategoryDTO(saved);
    }
    // Xử lý chuyển đổi categoryImage từ chuỗi base64 sang dạng byte[] (vì DB không lưu dạng chuỗi base64)
    public byte[] decodeCatogoryImage(String base64Image) {
        if (base64Image == null || base64Image.isBlank()) {
            return null;
        }
        // Loại bỏ phần tiền tố "data:image/png;base64,"
        int comma = base64Image.indexOf(',');
        String onlyBase64 = comma != -1 ? base64Image.substring(comma + 1) : base64Image;
        try {
            return java.util.Base64.getDecoder().decode(onlyBase64);
        } 
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Hình ảnh danh mục không hợp lệ",e);
        }
    }
    // LẤY DANH SÁCH LOẠI DANH MỤC THEO LOẠI (nếu type= null thì trả về danh sách trống)
    public List<CategoryDTO> getCategoryDTOByType(String type) {
        List<Category> categoriesByType = categoryRepository.findByType(type);
        String t = type.trim();
            categoriesByType = categoryRepository.findByType(t);
        
        return categoriesByType.stream().map(CategoryDTO::new).collect(Collectors.toList()); // return danh sách CategoryDTO được lọc theo type
    }

    // LẤY DANH SÁCH LOẠI DANH MỤC
    public List<String> getAllCategoryTypes() {
        return categoryRepository.findDistinctByType();
    }



}   