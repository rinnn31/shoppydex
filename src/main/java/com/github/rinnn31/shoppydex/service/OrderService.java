package com.github.rinnn31.shoppydex.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.rinnn31.shoppydex.exception.SPDException;
import com.github.rinnn31.shoppydex.model.dto.OrderModel;
import com.github.rinnn31.shoppydex.model.entity.OrderItemEntity;
import com.github.rinnn31.shoppydex.model.entity.ProductEntity;
import com.github.rinnn31.shoppydex.model.entity.UserEntity;
import com.github.rinnn31.shoppydex.repository.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    public void buyProduct(String username, Long productId, Integer quantity) {
        UserEntity user = userService.getUser(username);

        ProductEntity product = productService.getProductById(productId);
        if (product.getStock() < quantity) {
            throw new SPDException(102, "Số lượng sản phẩm trong kho không đủ!");
        }
        if (user.getPoints() < product.getPrice() * quantity) {
            throw new SPDException(103, "Số dư tài khoản không đủ để thực hiện giao dịch!");
        }

        List<String> purchasedItems = productService.takeItemsFromProduct(productId, quantity);
        
        userService.addUserPoints(username, -(product.getPrice() * quantity));

        OrderItemEntity order = new OrderItemEntity();
        order.setUser(user);
        order.setProductId(productId);
        order.setValues(purchasedItems);
        order.setProductName(product.getName());
        order.setUnitPrice(product.getPrice());
        order.setCount(quantity);
        order.setOrderDate(LocalDateTime.now());
        orderRepository.save(order);
    }

    public List<OrderModel> getOrdersByUser(String username) {
        UserEntity user = userService.getUser(username);
        Optional<List<OrderItemEntity>> orders = orderRepository.findAllByUserOrderByOrderDateDesc(user);
        if (orders.isEmpty()) return List.of();
        return orders.get().stream().map(OrderModel::new).toList();
    }
}
