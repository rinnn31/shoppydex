package com.github.rinnn31.shoppydex.model.entity;

import java.util.List;
import java.util.Map;

import com.github.rinnn31.shoppydex.utils.JsonObjectConverter;
import com.github.rinnn31.shoppydex.utils.StringArrayConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity(name = "ProductItem")
public class ProductItemEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="product_seq_gen")
    @SequenceGenerator(name="product_seq_gen", sequenceName="product_seq", allocationSize=50, initialValue=1000000)
    @Column(name = "ProductItemID")
    private Long productItemId;

    @Column(name = "Name")
    private String name;

    @Column(name = "Description")
    private String description;

    @Column(name = "Value", nullable = false)
    private String value;

    @Column(name = "Price")
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductID", referencedColumnName = "ProductID", nullable = false)
    private ProductEntity product;
    
    @Lob
    @Column(name = "Extras", nullable = true)
    @Convert(converter = JsonObjectConverter.class)
    private Map<String, Object> extras;

    // Lưu trữ danh sách đường dẫn hình ảnh, cách nhau bằng dấu phẩy
    @Column(name = "Images")
    @Lob
    @Convert(converter = StringArrayConverter.class)
    private List<String> images;

    public ProductItemEntity() {
    }

    public ProductItemEntity(String value,  ProductEntity product) {
        this.value = value;
        this.product = product;
        this.name = null;
        this.description = null;
        this.extras = null;
        this.price = null;
        this.images = null;
    }

    public Long getProductItemId() {
        return productItemId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, Object> extras) {
        this.extras = extras;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

}
