package com.crepestrips.orderservice.service;

import com.crepestrips.orderservice.client.FoodItemServiceClient;
import com.crepestrips.orderservice.dto.CartDto;
import com.crepestrips.orderservice.dto.DefaultResult;
import com.crepestrips.orderservice.dto.FoodItemDto;
import com.crepestrips.orderservice.dto.FoodItemServiceResponseDto;
import com.crepestrips.orderservice.model.Order;
import com.crepestrips.orderservice.model.OrderItem;
import com.crepestrips.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderCreationService {
    private static final Logger logger = LoggerFactory.getLogger(OrderCreationService.class);

    private final OrderRepository orderRepository;
    private final FoodItemServiceClient foodItemServiceClient;

    @Autowired
    public OrderCreationService(OrderRepository orderRepository, FoodItemServiceClient foodItemServiceClient) {
        this.orderRepository = orderRepository;
        this.foodItemServiceClient = foodItemServiceClient;
    }

    @Transactional
    public Order createOrderFromCart(CartDto cartDetails) {
        logger.info("Starting to create order for cartId: {}", cartDetails.getCartId());

        // Clean cart to remove suspended items
        DefaultResult cleanCartResult = foodItemServiceClient.cleanCart(cartDetails.getFoodItemIds());

        if (cleanCartResult == null || cleanCartResult.isError()) {
            logger.error("Failed to clean cart for cartId: {}", cartDetails.getCartId());
            return null;
        }

        // Extract the cleaned list of food item IDs
        List<String> cleanedFoodItemIds;
        if (cleanCartResult.getResult() instanceof List<?>) {
            try {
                @SuppressWarnings("unchecked")
                List<String> castedResult = (List<String>) cleanCartResult.getResult();
                cleanedFoodItemIds = castedResult;
                cartDetails.setFoodItemIds(cleanedFoodItemIds);
            } catch (ClassCastException e) {
                logger.error("Error casting cleaned cart result: {}", e.getMessage());
                return null;
            }
        } else {
            logger.error("Unexpected result type from cleanCart");
            return null;
        }

        // If the cleaned cart is empty, return null
        if (cleanedFoodItemIds.isEmpty()) {
            logger.warn("Cart is empty after cleaning");
            return null;
        }

        Map<String, Integer> itemQuantities = calculateItemQuantities(cleanedFoodItemIds);
        List<OrderItem> orderItems = new ArrayList<>();
        String orderLevelRestaurantId = null;
        List<String> allFoodItemIdsForStockDecrement = new ArrayList<>();
        boolean proceedWithOrderCreation = true;

        if (itemQuantities.isEmpty()) {
            return null;
        }

        for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
            String foodItemIdStringFromCart = entry.getKey();
            Integer quantity = entry.getValue();

            FoodItemServiceResponseDto responseWrapper;
            FoodItemDto foodItemInfo;
            try {
                responseWrapper = foodItemServiceClient.getFoodItemByIdWrapped(foodItemIdStringFromCart);
                if (responseWrapper != null && !responseWrapper.isError() && responseWrapper.getResult() != null) {
                    foodItemInfo = responseWrapper.getResult();
                } else {
                    continue;
                }
            } catch (Exception e) {
                logger.error("Error calling FoodItemService: {}", e.getMessage());
                continue;
            }

            if (foodItemInfo.getId() == null) {
                continue;
            }

            if (foodItemInfo.getRestaurantId() != null && !foodItemInfo.getRestaurantId().isBlank()) {
                if (orderLevelRestaurantId == null) {
                    orderLevelRestaurantId = foodItemInfo.getRestaurantId();
                } else if (!orderLevelRestaurantId.equals(foodItemInfo.getRestaurantId())) {
                    logger.error("Items from different restaurants found in the same cart");
                    proceedWithOrderCreation = false;
                    break;
                }
            }

            double priceForOrderItem = (foodItemInfo.getPrice() != null) ? foodItemInfo.getPrice() : 0.0;

            OrderItem orderItem = new OrderItem(
                    foodItemInfo.getId(),
                    foodItemInfo.getName(),
                    quantity,
                    priceForOrderItem);
            orderItems.add(orderItem);

            for (int i = 0; i < quantity; i++) {
                allFoodItemIdsForStockDecrement.add(foodItemInfo.getId());
            }
        }

        if (!proceedWithOrderCreation || orderItems.isEmpty() ||
                (orderLevelRestaurantId == null && !orderItems.isEmpty())) {
            return null;
        }

        // decrement stock before creating the order
        if (!allFoodItemIdsForStockDecrement.isEmpty()) {
            try {
                ResponseEntity<DefaultResult> stockDecrementResponse = foodItemServiceClient
                        .decrementStock(allFoodItemIdsForStockDecrement);

                if (stockDecrementResponse.getStatusCode().is2xxSuccessful() &&
                        stockDecrementResponse.getBody() != null &&
                        !stockDecrementResponse.getBody().isError()) {

                    // Create and save order only if stock decrement was successful
                    Order order = new Order(
                            cartDetails.getUserId(),
                            orderLevelRestaurantId,
                            cartDetails.getCartId());

                    for (OrderItem item : orderItems) {
                        order.addOrderItem(item);
                    }

                    Order savedOrder = orderRepository.save(order);
                    logger.info("Successfully created order: {}", savedOrder.getId());
                    return savedOrder;
                } else {
                    logger.error("Failed to decrement stock. Order will not be created.");
                    return null;
                }
            } catch (Exception e) {
                logger.error("Exception during stock decrement: {}", e.getMessage());
                return null;
            }
        } else {
            if (!orderItems.isEmpty()) {
                Order order = new Order(
                        cartDetails.getUserId(),
                        orderLevelRestaurantId,
                        cartDetails.getCartId());

                for (OrderItem item : orderItems) {
                    order.addOrderItem(item);
                }

                Order savedOrder = orderRepository.save(order);
                logger.info("Successfully created order: {}", savedOrder.getId());
                return savedOrder;
            } else {
                return null;
            }
        }
    }

    private Map<String, Integer> calculateItemQuantities(List<String> foodItemIds) {
        Map<String, Integer> quantities = new HashMap<>();
        if (foodItemIds == null)
            return quantities;
        for (String itemId : foodItemIds) {
            quantities.put(itemId, quantities.getOrDefault(itemId, 0) + 1);
        }
        return quantities;
    }
}