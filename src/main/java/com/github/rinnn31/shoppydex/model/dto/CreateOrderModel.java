package com.github.rinnn31.shoppydex.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateOrderModel {
    @NotNull(message = "Product ID không được để trống")
    private Long productId;

    @NotNull(message = "Số lượng không được để trống")
    @Positive(message = "Số lượng phải là số dương")
    private Integer quantity;

    public CreateOrderModel() {}

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
