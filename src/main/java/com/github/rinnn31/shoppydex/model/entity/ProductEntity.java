package com.github.rinnn31.shoppydex.model.entity;

import java.util.List;

import com.github.rinnn31.shoppydex.utils.StringArrayConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;

@Entity(name = "Product")
public class ProductEntity {
    public static final String TYPE_UNIQUE_ITEMS = "UNIQUE_ITEM";
    public static final String TYPE_MULTI_ITEMS = "MULTI_ITEM";

    @Id
    @Column(name = "ProductID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="category_seq_gen")
    @SequenceGenerator(name="category_seq_gen", sequenceName="category_seq", allocationSize=1, initialValue=1000000)
    private Long productId;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Description")
    private String description;

    @Column(name = "Price")
    private Double price;

    @Column(name = "Stock", nullable = false)
    private Integer stock = 0;

    @Column(name = "Category", nullable = false)
    private String category;

    @Column(name = "ProductType", nullable = false)
    private String productType;

    // Lưu trữ danh sách đường dẫn hình ảnh, cách nhau bằng dấu phẩy
    @Column(name = "Images")
    @Lob
    @Convert(converter = StringArrayConverter.class)
    private List<String> images;

    public ProductEntity() {
        // Default constructor for JPA
    }

    public ProductEntity(String name, String category, String productType) {
        this.name = name;
        this.description = null;
        this.price = null;
        this.category = category;
        this.productType = productType;
        this.stock = 0;
        this.images = null;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productItemType) {
        if (TYPE_UNIQUE_ITEMS.equals(productItemType) || TYPE_MULTI_ITEMS.equals(productItemType)) {
            this.productType = productItemType;
        } else {
            throw new IllegalArgumentException("Invalid product type: " + productItemType);
        }
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
