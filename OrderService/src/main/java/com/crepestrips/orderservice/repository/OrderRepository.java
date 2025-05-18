package com.crepestrips.orderservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crepestrips.orderservice.model.Order;
import com.crepestrips.orderservice.model.OrderPriority;
import com.crepestrips.orderservice.model.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<List<Order>> findByRestaurantId(String restaurantId);

    Optional<List<Order>> findByUserId(UUID userId);

    Optional<List<Order>> findByPriority(OrderPriority priority);

    Optional<List<Order>> findByStatus(OrderStatus status);

    Optional<List<Order>> findByUserIdAndStatus(UUID userId, OrderStatus status);

    Optional<List<Order>> findByRestaurantIdAndStatus(String restaurantId, OrderStatus status);

    Optional<List<Order>> findByUserIdAndPriority(UUID userId, OrderPriority priority);

    Optional<List<Order>> findByRestaurantIdAndPriority(String restaurantId, OrderPriority priority);
}