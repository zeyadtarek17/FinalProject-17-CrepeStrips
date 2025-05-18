package com.crepestrips.orderservice.service;

import com.crepestrips.orderservice.client.FoodItemServiceClient; // Import Feign client
import com.crepestrips.orderservice.dto.CartDto;
import com.crepestrips.orderservice.dto.FoodItemDto;         // Import DTO
import com.crepestrips.orderservice.model.Order;
import com.crepestrips.orderservice.model.OrderItem;
import com.crepestrips.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderCreationService {
    private static final Logger logger = LoggerFactory.getLogger(OrderCreationService.class);

    private final OrderRepository orderRepository;
    private final FoodItemServiceClient foodItemServiceClient; // <<< INJECTED

    @Autowired
    public OrderCreationService(OrderRepository orderRepository, FoodItemServiceClient foodItemServiceClient) { // <<< ADDED TO CONSTRUCTOR
        this.orderRepository = orderRepository;
        this.foodItemServiceClient = foodItemServiceClient;
    }

    @Transactional
    public Order createOrderFromCart(CartDto cartDetails) {
        logger.info("OrderCreationService: Starting to create order for cartId: {}", cartDetails.getCartId());

        Map<String, Integer> itemQuantities = calculateItemQuantities(cartDetails.getFoodItemIds());
        logger.info("Calculated item quantities: {}", itemQuantities);

        List<OrderItem> orderItems = new ArrayList<>();
        String firstItemRestaurantId = null; // To potentially get a common restaurant ID

        if (itemQuantities.isEmpty()) {
            logger.warn("No items found in cart {}. Order might be created empty or this could be an error.", cartDetails.getCartId());
            // Depending on business rules, you might throw an exception here if an order must have items.
        }

        for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
            String foodItemIdString = entry.getKey(); // This is the ID from CartDto (String)
            Integer quantity = entry.getValue();
            // UUID foodItemIdForOrderItem; // This will be the UUID version for OrderItem

            // We don't need to parse foodItemIdString to UUID here yet,
            // as the Feign client takes a String ID.
            // The FoodItemDto.id will be a String, which we then parse for OrderItem.

            FoodItemDto foodItemInfo = null;
            try {
                logger.debug("Calling FoodItemService for food item ID (string): {}", foodItemIdString);
                foodItemInfo = foodItemServiceClient.getFoodItemById(foodItemIdString); // <<< ACTUAL FEIGN CALL
                logger.debug("Received from FoodItemService: {}", foodItemInfo);

            } catch (Exception e) { // Catch FeignException or more general exceptions
                logger.error("Error calling FoodItemService for food item ID {}: {}. Skipping this item.", foodItemIdString, e.getMessage());
                // TODO: More sophisticated error handling:
                // - Retry?
                // - Mark item as unavailable?
                // - Fail entire order?
                // For now, we skip the item.
                continue;
            }

            if (foodItemInfo == null || foodItemInfo.getId() == null) { // Check if service returned valid data
                logger.warn("No valid information (or null ID) received from FoodItemService for food item ID string {}. Skipping item.", foodItemIdString);
                continue;
            }

            // Convert String ID from DTO to UUID for OrderItem
            UUID foodItemIdForOrderItem;
            try {
                foodItemIdForOrderItem = UUID.fromString(foodItemInfo.getId());
            } catch (IllegalArgumentException e) {
                logger.error("FoodItemService returned an ID '{}' that is not a valid UUID for DTO ID string {}. Skipping item.", foodItemInfo.getId(), foodItemIdString);
                continue;
            }
            
            // Capture the restaurantId from the first valid item (simple strategy)
            if (firstItemRestaurantId == null && foodItemInfo.getRestaurantId() != null) {
                firstItemRestaurantId = foodItemInfo.getRestaurantId();
            }


            double priceForOrderItem;
            if (foodItemInfo.getPrice() != null) {
                priceForOrderItem = foodItemInfo.getPrice(); // Convert Double to BigDecimal
            } else {
                priceForOrderItem = 0.0; // Default or handle as error
                logger.warn("FoodItemDTO for id {} has null price. Defaulting to 0.", foodItemInfo.getId());
            }

            logger.info("Processing item: ID={}, Name={}, Price={}, Quantity={}",
                        foodItemInfo.getId(), foodItemInfo.getName(), priceForOrderItem, quantity);

            OrderItem orderItem = new OrderItem(
                    foodItemIdForOrderItem,    // Use the UUID
                    foodItemInfo.getName(),    // Use actual name from DTO
                    quantity,
                    priceForOrderItem          // Use actual price (converted to BigDecimal) from DTO
            );
            orderItems.add(orderItem);
        }

        if (orderItems.isEmpty() && !itemQuantities.isEmpty()) {
            logger.error("No valid order items could be created after fetching from FoodItemService for cart {}.", cartDetails.getCartId());
            throw new RuntimeException("Failed to create any valid order items for cart " + cartDetails.getCartId() + " after FoodItemService lookup.");
        }
        
        UUID orderRestaurantId = null;
        if (firstItemRestaurantId != null) {
            try {
                orderRestaurantId = UUID.fromString(firstItemRestaurantId);
            } catch (IllegalArgumentException e) {
                logger.warn("The derived restaurantId '{}' is not a valid UUID. Order will have null restaurantId.", firstItemRestaurantId);
            }
        } else {
            logger.warn("Could not determine a restaurantId for the order. Order will have null restaurantId.");
            // Potentially use a default or throw an error if restaurantId is mandatory for an Order
        }


        Order order = new Order(
                cartDetails.getUserId(),
                orderRestaurantId, // Use determined (or null) restaurantId
                cartDetails.getCartId()
        );

        for (OrderItem item : orderItems) {
            order.addOrderItem(item);
        }
        // order.calculateTotalAmount(); // addOrderItem should handle this

        Order savedOrder = orderRepository.save(order);
        logger.info("OrderCreationService: Successfully created order {} with total amount: {}", savedOrder.getId(), savedOrder.getTotalAmount());
        return savedOrder;
    }

    private Map<String, Integer> calculateItemQuantities(List<String> foodItemIds) {
        Map<String, Integer> quantities = new HashMap<>();
        if (foodItemIds == null) return quantities;
        for (String itemId : foodItemIds) {
            quantities.put(itemId, quantities.getOrDefault(itemId, 0) + 1);
        }
        return quantities;
    }
}