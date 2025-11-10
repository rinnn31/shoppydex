package com.github.rinnn31.shoppydex.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.rinnn31.shoppydex.model.dto.ApiResponse;
import com.github.rinnn31.shoppydex.model.dto.CreateOrderModel;
import com.github.rinnn31.shoppydex.model.dto.OrderModel;
import com.github.rinnn31.shoppydex.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderApiController {
    
    @Autowired
    private OrderService orderService;

    @GetMapping
    public ApiResponse<?> getOrders() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<OrderModel> orders = orderService.getOrdersByUser(username);
        return ApiResponse.success(orders);
    }

    @PostMapping("/buy")
    public ApiResponse<?> buyProduct(@Valid @RequestBody CreateOrderModel request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        orderService.buyProduct(username, request.getProductId(), request.getQuantity());
        return ApiResponse.success("Mua sản phẩm thành công");
    }
}
