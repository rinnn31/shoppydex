package com.github.rinnn31.shoppydex.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.github.rinnn31.shoppydex.model.entity.OrderItemEntity;

public class OrderModel {
    private Long orderId;

    private Long productId;

    private List<String> values;

    private Integer unitPrice;

    private Integer count;

    private String productName;

    private LocalDateTime orderDate;

    public OrderModel() {
        // Default constructor
    }

    public OrderModel(OrderItemEntity entity) {
        this.orderId = entity.getOrderId();
        this.productId = entity.getProductId();
        this.values = entity.getValues();
        this.unitPrice = entity.getUnitPrice();
        this.count = entity.getCount();
        this.productName = entity.getProductName();
        this.orderDate = entity.getOrderDate();
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public List<String> getValues() {
        return values;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public Integer getCount() {
        return count;
    }

    public String getProductName() {
        return productName;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setCount(Integer count) {
        this.count = count;
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
