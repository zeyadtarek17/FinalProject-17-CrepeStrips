package com.crepestrips.orderservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crepestrips.orderservice.model.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    // Find all items for a specific order
    Optional<List<OrderItem>> findByOrderId(UUID orderId);

}