package com.github.rinnn31.shoppydex.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.rinnn31.shoppydex.exception.SPDException;
import com.github.rinnn31.shoppydex.model.dto.ProductModel;
import com.github.rinnn31.shoppydex.model.dto.ProductsPageModel;
import com.github.rinnn31.shoppydex.model.entity.ProductEntity;
import com.github.rinnn31.shoppydex.repository.ProductRepository;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Value("${app.products.images.dir}")
    private String uploadsDir;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductItemService productItemService;

    public ProductEntity getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(
            () -> new SPDException(101, "Sản phẩm không tồn tại!")
        );
    }

    public void deleteAllProducts(String category) {
        List<ProductEntity> products = productRepository.findByCategory(category);
        for (ProductEntity product : products) {
            productItemService.deleteAllItems(String.valueOf(product.getProductId()));
        }
        productRepository.deleteAll(products);
    }
    
    public List<String> getAllCategories() {
        return productRepository.findDistinctCategories();
    }

    public void addProduct(ProductModel productModel) {
        if (productRepository.existsByName(productModel.getName())) {
            throw new SPDException(101, "Tên sản phẩm đã tồn tại!");
        }

        String imagePath = null;
        if (productModel.getImage() != null) {
            imagePath = storeProductImage(productModel.getImage());
        }

        ProductEntity newProduct = new ProductEntity();
        newProduct.setName(productModel.getName());
        newProduct.setDescription(productModel.getDescription());
        newProduct.setPrice(productModel.getPrice());
        newProduct.setCategory(productModel.getCategory());
        newProduct.setImage(imagePath);
        newProduct.setCreateAt(LocalDateTime.now());
        productRepository.save(newProduct);
    }

    public void deleteProduct(Long productId) {
        ProductEntity product = getProductById(productId);
        deleteProductImage(product.getImage());
        productRepository.deleteById(productId);
        productItemService.deleteAllItems(String.valueOf(productId));
        
    }

    public ProductsPageModel getProducts(List<String> categories, int page, int size) {
        Page<ProductEntity> products;
        Pageable pageable = (Pageable)PageRequest.of(page, size);
        if (categories != null && !categories.isEmpty()) {
            products = productRepository.findByCategoryIn(categories, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }
        return new ProductsPageModel(products.map(ProductModel::new));
    }

    public ProductModel getProduct(Long productId) {
        ProductEntity product = getProductById(productId);
        return new ProductModel(product);
    }

    public void updateProduct(ProductModel productModel) {
        ProductEntity existingProduct = getProductById(productModel.getProductId());

        if (productModel.getName() != null) {
            if (!existingProduct.getName().equals(productModel.getName()) && productRepository.existsByName(productModel.getName())) {
                throw new SPDException(102, "Tên sản phẩm đã tồn tại!");
            }
            existingProduct.setName(productModel.getName());
        }

        if (productModel.getDescription() != null) {
            existingProduct.setDescription(productModel.getDescription());
        }
        if (productModel.getPrice() != null) {
            existingProduct.setPrice(productModel.getPrice());
        }
        if (productModel.getCategory() != null) {
            existingProduct.setCategory(productModel.getCategory());
        }
        
        if (productModel.getImage() != null) {
            deleteProductImage(existingProduct.getImage());
            String newImagePath = storeProductImage(productModel.getImage());
            existingProduct.setImage(newImagePath);
        }
        
        productRepository.save(existingProduct);
    }

    public void addItemsToProduct(Long productId, List<String> items) {
        ProductEntity product = getProductById(productId);
        product.setStock(product.getStock() + items.size());
        productRepository.save(product);

        productItemService.appendItems(String.valueOf(productId), items);
    }

    public void clearItemsFromProduct(Long productId) {
        ProductEntity product = getProductById(productId);
        product.setStock(0);
        productRepository.save(product);

        productItemService.deleteAllItems(String.valueOf(productId));
    }

    public List<String> takeItemsFromProduct(Long productId, Integer quantity) {
        ProductEntity product = getProductById(productId);
        if (product.getStock() < quantity) {
            throw new SPDException(103, "Không đủ số lượng sản phẩm trong kho!");
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
        return productItemService.takeItems(String.valueOf(productId), quantity);
    }
    
    private String storeProductImage(String encodedImage) {
        String imageId = UUID.randomUUID().toString();

        Path imagePath = Paths.get(uploadsDir, imageId + ".png");
        try {
            byte[] imageBytes = Base64.getDecoder().decode(encodedImage);
            Files.write(imagePath, imageBytes);
            return imagePath.toString();
        } catch (IOException e) {
            logger.error("Lỗi khi lưu hình ảnh sản phẩm: {}", e.getMessage());
            return null;
        }
    }

    private void deleteProductImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return;
        }
        Path path = Paths.get(imagePath);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            logger.error("Lỗi khi xóa hình ảnh sản phẩm: {}", e.getMessage());
        }
    }
} 
