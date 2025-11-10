package com.github.rinnn31.shoppydex.model.entity;

import java.time.LocalDateTime;
import java.util.List;

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
import jakarta.persistence.Table;

@Entity
@Table(name = "OrderItem")
public class OrderItemEntity {
    @Id
    @Column(name= "OrderId", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="order_seq_gen")
    @SequenceGenerator(name="order_seq_gen", sequenceName="order_seq", allocationSize=1, initialValue=5000000)
    private long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", nullable = false, referencedColumnName = "UserId")
    private UserEntity user;

    @Lob
    @Column(name = "ProductValues", nullable = false)
    @Convert(converter = StringArrayConverter.class)
    private List<String> values;

    @Column(name = "ProductId", nullable = false)
    private long productId;

    @Column(name = "ProductName", nullable = false)
    private String productName;

    @Column(name = "UnitPrice", nullable = false)
    private int unitPrice;

    @Column(name = "Count", nullable = false)
    private int count;

    @Column(name = "OrderDate", nullable = false)
    private LocalDateTime orderDate;

    public OrderItemEntity() {
        // Default constructor for JPA
    }

    public OrderItemEntity(Long orderId, UserEntity owner, List<String> values, long productId, String productName, int unitPrice, int count, LocalDateTime orderDate) {
        this.orderId = orderId;
        this.user = owner;
        this.values = values;   
        this.productId = productId;
        this.unitPrice = unitPrice;
        this.productName = productName;
        this.count = count;
        this.orderDate = orderDate;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity owner) {
        this.user = owner;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

}
