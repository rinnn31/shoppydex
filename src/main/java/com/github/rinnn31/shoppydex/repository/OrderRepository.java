package com.github.rinnn31.shoppydex.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.rinnn31.shoppydex.model.entity.OrderItemEntity;
import com.github.rinnn31.shoppydex.model.entity.UserEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderItemEntity, Long> { 
    Optional<List<OrderItemEntity>> findAllByUserOrderByOrderDateDesc(UserEntity user);

    void deleteAllByUser(UserEntity user);
}
