package com.github.rinnn31.shoppydex.service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.rinnn31.shoppydex.exception.SPDException;
import com.github.rinnn31.shoppydex.model.api.ProductItemModel;
import com.github.rinnn31.shoppydex.model.api.ProductModel;
import com.github.rinnn31.shoppydex.model.entity.ProductEntity;
import com.github.rinnn31.shoppydex.model.entity.ProductItemEntity;
import com.github.rinnn31.shoppydex.repository.ProductItemRepository;
import com.github.rinnn31.shoppydex.repository.ProductRepository;
import com.github.rinnn31.shoppydex.utils.StringHelper;
import com.github.rinnn31.shoppydex.utils.WebImageResolver;

@Service
public class ProductService {
    @Autowired
    private ProductItemRepository productItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public boolean isMultiTypeProduct(Long productId) {
        ProductEntity product = getProductById(productId);
        return "MULTI_ITEM".equals(product.getProductType());
    }

    public ProductEntity getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(
            () -> new SPDException(101, "Danh mục không tồn tại!")
        );
    }

    public ProductItemEntity getProductItemEntityById(Long itemId) {
        return productItemRepository.findById(itemId).orElseThrow(
            () -> new SPDException(101, "Sản phẩm không tồn tại!")
        );
    }

    public void deleteAllProducts(String category) {
        List<ProductEntity> products = productRepository.findByCategory(category);
        for (ProductEntity product : products) {
            productItemRepository.deleteByProduct_ProductId(product.getProductId());
        }
        productRepository.deleteAll(products);
    }
    
    public List<String> getAllCategories() {
        return productRepository.findDistinctCategories();
    }

    public void addProduct(ProductModel productModel) {
        if (productRepository.existsByName(productModel.getName())) {
            throw new SPDException(101, "Tên danh mục đã tồn tại!");
        }
        if ("MULTI_ITEM".equals(productModel.getProductType()) && productModel.getPrice() == null) {
            throw new SPDException(102, "Danh mục MULTI_ITEM phải có giá sản phẩm!");
        }

        ArrayList<String> images = null;
        if (productModel.getImages() != null) {
            images = new ArrayList<>();
            for (String imageData : productModel.getImages()) {
                String imageName = StringHelper.normalizeString(productModel.getName()) + "_" + StringHelper.generateRandomHexString(12);
                String savedImagePath = WebImageResolver.pushImage(imageName, imageData);
                if (savedImagePath == null) {
                    throw new SPDException(103, "Lỗi khi lưu hình ảnh sản phẩm!");
                }
            }
        }

        ProductEntity newProduct = new ProductEntity();
        newProduct.setName(productModel.getName());
        newProduct.setDescription(productModel.getDescription());
        newProduct.setPrice(productModel.getPrice());
        newProduct.setCategory(productModel.getCategory());
        newProduct.setProductType(productModel.getProductType());
        newProduct.setImages(images);

        productRepository.save(newProduct);
    }

    public void deleteProduct(Long productId) {
        if (!productItemRepository.existsById(productId)) {
            throw new SPDException(101, "Sản phẩm không tồn tại!");
        }
        productItemRepository.deleteById(productId);

        // Xoá hết cản sản phẩm con nếu có
        productItemRepository.deleteByProduct_ProductId(productId);
    }

    public List<ProductModel> getProducts(String category) {
        List<ProductEntity> products;
        if (StringUtils.hasText(category)) {
            products = productRepository.findByCategory(category);
        } else {
            products = productRepository.findAll();
        }
        return products.stream().map(ProductModel::new).collect(Collectors.toList());
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
        if (productModel.getPrice() != null && "MULTI_ITEM".equals(existingProduct.getProductType())) {
            existingProduct.setPrice(productModel.getPrice());
        }
        if (productModel.getCategory() != null) {
            existingProduct.setCategory(productModel.getCategory());
        }
        if (productModel.getImages() != null) {
            ArrayList<String> images = new ArrayList<>();

            for (String imageData : productModel.getImages()) {
                String imageName = StringHelper.normalizeString(productModel.getName()) + "_" + StringHelper.generateRandomHexString(12);
                String savedImagePath = WebImageResolver.pushImage(imageName, imageData);
                if (savedImagePath == null) {
                    // Xoá hết các hình ảnh vừa lưu thành công trước đó
                    for (String imgPath : images) {
                        try {
                            Files.delete(Path.of(imgPath));
                        } catch (IOException ex) {
                            /* ignored */
                        }
                    }
                    throw new SPDException(103, "Lỗi khi lưu hình ảnh sản phẩm!");
                }
            }
            existingProduct.setImages(images);
            // Xoá hết các hình ảnh cũ
            for (String imgPath : existingProduct.getImages()) {
                try {
                    Files.delete(Path.of(imgPath));
                } catch (IOException ex) {
                    /* ignored */
                }
            }
        }

        productRepository.save(existingProduct);
    }

    public void addMultiTypeItems(Long productId, List<String> bulkItems) {
        ProductEntity category = getProductById(productId);
        if (!"MULTI_ITEM".equals(category.getProductType())) {
            throw new SPDException(102, "Chỉ có thể thêm sản phẩm vào danh mục MULTI_ITEM!");
        }

        ArrayList<ProductItemEntity> newItems = new ArrayList<>();
        for (String itemValue : bulkItems) {
            ProductItemEntity newItem = new ProductItemEntity();
            newItem.setValue(itemValue);
        }
        productItemRepository.saveAll(newItems);

        // Cập nhat số lượng tồn trong ProductEntity
        category.setStock(category.getStock() + newItems.size());
    }

    public void addUniqueTypeItem(Long productId, ProductItemModel productItemModel) {
        ProductEntity product = getProductById(productId);

        if (!"UNIQUE_ITEM".equals(product.getProductType())) {
            throw new SPDException(102, "Chỉ có thể thêm sản phẩm vào danh mục UNIQUE_ITEM!");
        }
        if (!StringUtils.hasText(productItemModel.getName())) {
            throw new SPDException(103, "Tên sản phẩm không được để trống!");
        }

        ArrayList<String> images = null;
        if (productItemModel.getImages() != null) {
            images = new ArrayList<>();
            for (String imageData : productItemModel.getImages()) {
                String imageName = StringHelper.normalizeString(productItemModel.getName()) + "_" + StringHelper.generateRandomHexString(12);
                String savedImagePath = WebImageResolver.pushImage(imageName, imageData);
                if (savedImagePath == null) {
                    throw new SPDException(103, "Lỗi khi lưu hình ảnh sản phẩm!");
                }
            }
        }

        ProductItemEntity newItem = new ProductItemEntity();
        newItem.setName(productItemModel.getName());
        newItem.setValue(productItemModel.getValue());
        newItem.setDescription(productItemModel.getDescription());
        newItem.setPrice(productItemModel.getPrice());
        newItem.setExtras(productItemModel.getExtras());
        newItem.setProduct(product);
        newItem.setImages(images);
        productItemRepository.save(newItem);

        // Câp nhật số lượng tồn trong ProductEntity
        product.setStock(product.getStock() + 1);
        productRepository.save(product);
    }

    public ProductItemModel getProductItemById(Long itemId, boolean adminView) {
        ProductItemEntity item = productItemRepository.findById(itemId).orElseThrow(
            () -> new SPDException(101, "Sản phẩm không tồn tại!")
        );
        if (!adminView) {
            if (item.getProduct().getProductType().equals("MULTI_ITEM")) {
                throw new SPDException(102, "Chỉ admin mới có thể xem sản phẩm loại MULTI_ITEM");
            }
        }
        return new ProductItemModel(item);
    }

    public void deleteUniqueTypeItem(Long itemId) {
        ProductItemEntity item = productItemRepository.findById(itemId).orElseThrow(
            () -> new SPDException(101, "Sản phẩm không tồn tại!")
        );
        
        if (item.getProduct().getProductType().equals("UNIQUE_ITEM")) {
            ProductEntity product = item.getProduct();
            product.setStock(product.getStock() - 1);
            productRepository.save(product);
        } else {
            throw new SPDException(102, "Chỉ có thể đơn xoá sản phẩm thuộc lại UNIQUE_ITEM!");
        }

        productItemRepository.deleteById(itemId);
    }
    
    public void deleteMultiTypeItems(Long productId, Integer quantity) {
        ProductEntity product = productRepository.findById(productId).orElseThrow(
            () -> new SPDException(101, "Danh mục không tồn tại!")
        );
        if (!"MULTI_ITEM".equals(product.getProductType())) {
            throw new SPDException(102, "Chỉ có thể xoá sản phẩm từ danh mục MULTI_ITEM!");
        }

        productItemRepository.deleteTopNByProduct_ProductId(productId, quantity);
        product.setStock(product.getStock() - quantity);
        if (product.getStock() < 0) {
            product.setStock(0);
        }
        productRepository.save(product);
    }

    public void deleteAllProductItems(Long productId) {
        ProductEntity product = productRepository.findById(productId).orElseThrow(
            () -> new SPDException(101, "Danh mục không tồn tại!")
        );

        productItemRepository.deleteByProduct_ProductId(productId);
        product.setStock(0);
        productRepository.save(product);
    }

    public void updateUniqueTypeItem(ProductItemModel productItemModel) {
        if (!productItemRepository.existsById(productItemModel.getProductItemId())) {
            throw new SPDException(101, "Sản phẩm không tồn tại!");
        }

        ProductItemEntity existingItem = productItemRepository.findById(productItemModel.getProductItemId()).get();
        if (existingItem.getProduct().getProductType().equals("MULTI_ITEM")) {
            throw new SPDException(102, "Chỉ có thể cập nhật sản phẩm trong danh mục UNIQUE_ITEM!");
        }

        if (productItemModel.getName() != null) {
            existingItem.setName(productItemModel.getName());
        }
        if (productItemModel.getDescription() != null) {
            existingItem.setDescription(productItemModel.getDescription());
        }
        if (productItemModel.getValue() != null) {
            existingItem.setValue(productItemModel.getValue());
        }
        if (productItemModel.getPrice() != null) {
            existingItem.setPrice(productItemModel.getPrice());
        }
        if (productItemModel.getExtras() != null) {
            existingItem.setExtras(productItemModel.getExtras());
        }
        if (productItemModel.getImages() != null) {
            ArrayList<String> images = new ArrayList<>();

            for (String imageData : productItemModel.getImages()) {
                String imageName = StringHelper.normalizeString(productItemModel.getName()) + "_" + StringHelper.generateRandomHexString(12);
                String savedImagePath = WebImageResolver.pushImage(imageName, imageData);
                if (savedImagePath == null) {
                    // Xoá hết các hình ảnh vừa lưu thành công trước đó
                    for (String imgPath : images) {
                        try {
                            Files.delete(Path.of(imgPath));
                        } catch (IOException ex) {
                            /* ignored */
                        }
                    }
                    throw new SPDException(103, "Lỗi khi lưu hình ảnh sản phẩm!");
                }
            }
            existingItem.setImages(images);
            // Xoá hết các hình ảnh cũ
            for (String imgPath : existingItem.getImages()) {
                try {
                    Files.delete(Path.of(imgPath));
                } catch (IOException ex) {
                    /* ignored */
                }
            }
        }

        productItemRepository.save(existingItem);
    }

    public List<ProductItemModel> getUniqueTypeItems(Long productId, boolean adminView) {
        ProductEntity product = productRepository.findById(productId).orElseThrow(
            () -> new SPDException(101, "Danh mục không tồn tại!")
        );
        if (!"UNIQUE_ITEM".equals(product.getProductType())) {
            throw new SPDException(102, "Chỉ có thể lấy sản phẩm từ danh mục UNIQUE_ITEM!");
        }
        List<ProductItemEntity> items = productItemRepository.findAllByProduct_ProductId(productId);
        List<ProductItemModel> result = items.stream().map(ProductItemModel::new).collect(Collectors.toList());
        result.forEach((model) -> {
            if (!adminView) {
                model.setValue(null);
            }
        });
        return result;
    }

    public List<String> getMultiTypeItems(Long productId) {
        ProductEntity product = productRepository.findById(productId).orElseThrow(
            () -> new SPDException(101, "Danh mục không tồn tại!")
        );
        if (!"MULTI_ITEM".equals(product.getProductType())) {
            throw new SPDException(102, "Chỉ có thể lấy sản phẩm từ danh mục MULTI_ITEM!");
        }
        List<ProductItemEntity> items = productItemRepository.findAllByProduct_ProductId(productId);
        return items.stream().map(ProductItemEntity::getValue).collect(Collectors.toList());
    }

    public ProductItemModel takeUniqueTypeItem(Long itemId) {
        ProductItemEntity item = productItemRepository.findById(itemId).orElseThrow(
            () -> new SPDException(101, "Sản phẩm không tồn tại!")
        );
        if (!"UNIQUE_ITEM".equals(item.getProduct().getProductType())) {
            throw new SPDException(102, "Chỉ có thể lấy giá trị sản phẩm từ danh mục UNIQUE_ITEM!");
        }
        
        ProductEntity product = item.getProduct();
        product.setStock(product.getStock() - 1);
        productRepository.save(product);

        productItemRepository.deleteById(itemId);
        return new ProductItemModel(item);
    }

    public List<String> takeMultiTypeItems(Long productId, Integer quantity) {
        ProductEntity product = productRepository.findById(productId).orElseThrow(
            () -> new SPDException(101, "Sản phẩm không tồn tại!")
        );
        if (!"MULTI_ITEM".equals(product.getProductType())) {
            throw new SPDException(102, "Chỉ có thể lấy giá trị sản phẩm từ danh mục MULTI_ITEM!");
        }

        List<ProductItemEntity> items = productItemRepository.findTopNByProduct_ProductId(productId, quantity);
        if (items.size() < quantity) {
            throw new SPDException(103, "Số lượng sản phẩm trong kho không đủ!");
        }

        product.setStock(product.getStock() - items.size());
        productRepository.save(product);

        productItemRepository.deleteTopNByProduct_ProductId(productId, quantity);
        return items.stream().map(ProductItemEntity::getValue).collect(Collectors.toList());
    }
} 
