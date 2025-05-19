package com.crepestrips.orderservice.strategy; // Or your chosen package

import com.crepestrips.orderservice.model.Order;

public interface OrderStatusStrategy {

    String getStatusDetails(Order order);
}