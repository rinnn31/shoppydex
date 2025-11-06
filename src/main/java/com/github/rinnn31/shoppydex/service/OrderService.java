package com.github.rinnn31.shoppydex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.rinnn31.shoppydex.exception.SPDException;
import com.github.rinnn31.shoppydex.model.entity.ProductItemEntity;
import com.github.rinnn31.shoppydex.model.entity.UserEntity;
import com.github.rinnn31.shoppydex.repository.OrderRepository;
import com.github.rinnn31.shoppydex.repository.ProductItemRepository;
import com.github.rinnn31.shoppydex.repository.ProductRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductItemRepository productItemRepository;

    public void buyUniqueTypeItem(String username, Long productItemId) {
        UserEntity user = userService.getUser(username);

        ProductItemEntity productItem = productItemRepository.findById(productItemId)
                .orElseThrow(() -> new SPDException(102, "Sản phẩm không tồn tại"));
        if (productItem.getPrice() > user.getPoints()) {
            throw new SPDException(103, "Số dư không đủ để thực hiện giao dịch");
        }
       
        user.setPoints(user.getPoints() - productItem.getPrice());


    }
}
