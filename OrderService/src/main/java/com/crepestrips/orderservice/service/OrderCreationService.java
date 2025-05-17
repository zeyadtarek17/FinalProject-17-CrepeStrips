package com.crepestrips.orderservice.service;

import com.crepestrips.orderservice.dto.CartDto;
import com.crepestrips.orderservice.model.Order; // Your Order entity
import com.crepestrips.orderservice.model.OrderItem; // Your OrderItem entity
import com.crepestrips.orderservice.repository.OrderRepository;
// import com.crepestrips.orderservice.client.FoodItemServiceClient; // You'll create this later
// import com.crepestrips.orderservice.dto.FoodItemDTO; // You'll use this later
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
    // private final FoodItemServiceClient foodItemServiceClient; // Inject when ready

    @Autowired
    public OrderCreationService(OrderRepository orderRepository /*, FoodItemServiceClient foodItemServiceClient */) {
        this.orderRepository = orderRepository;
        // this.foodItemServiceClient = foodItemServiceClient;
    }

    @Transactional // Make the whole order creation process transactional
    public Order processOrderPlacement(CartDto cartDetails) {
        logger.info("Starting to process order for cartId: {}", cartDetails.getCartId());

        // 1. Process foodItemIds to get quantities
        Map<String, Integer> itemQuantities = calculateItemQuantities(cartDetails.getFoodItemIds());
        logger.info("Calculated item quantities: {}", itemQuantities);

        // --- This is where Feign client calls to FoodItemService will go ---
        // For now, let's simulate this part with dummy data
        List<OrderItem> orderItems = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
            String foodItemIdString = entry.getKey();
            Integer quantity = entry.getValue();

            // SIMULATE FoodItemService call
            // FoodItemDTO foodItemInfo = foodItemServiceClient.getFoodItemById(UUID.fromString(foodItemIdString));
            // For now, use dummy data:
            String foodItemName = "Dummy " + foodItemIdString;
            double pricePerUnit = 9.99; // Dummy price
            UUID foodItemIdUUID = null;
            try {
                foodItemIdUUID = UUID.fromString(foodItemIdString); // Assuming foodItemIds are valid UUID strings
            } catch (IllegalArgumentException e) {
                logger.error("Invalid UUID format for foodItemId: {}. Skipping this item.", foodItemIdString);
                // Decide how to handle this: skip item, fail order, etc.
                continue; // For now, skip
            }


            logger.info("Processing item: ID={}, Name={}, Price={}, Quantity={}",
                        foodItemIdUUID, foodItemName, pricePerUnit, quantity);

            OrderItem orderItem = new OrderItem(
                    foodItemIdUUID,       // Assuming foodItemIdString is a valid UUID
                    foodItemName,
                    quantity,
                    pricePerUnit
            );
            // orderItem.calculateSubTotal(); // Constructor should do this
            orderItems.add(orderItem);
        }
        // --- End of Feign client simulation ---


        if (orderItems.isEmpty() && !itemQuantities.isEmpty()) {
            logger.error("No valid order items could be created from cart {}. Aborting order.", cartDetails.getCartId());
            // Handle this scenario, maybe throw an exception or return null/specific status
            // This could happen if all foodItemIds were invalid UUIDs or FoodItemService returned errors for all.
            throw new RuntimeException("Failed to create any valid order items for cart " + cartDetails.getCartId());
        }


        // 2. Create the Order entity
        // Assuming cartDetails.getUserId() is the restaurantId for now, or you get it from FoodItemService
        // You'll need to decide where restaurantId comes from. If each food item can be from a different
        // restaurant, the model is more complex. If all items in a cart are from one restaurant,
        // that restaurantId might need to be part of the CartDto or fetched.
        // For now, let's assume a placeholder or that it's not strictly needed for the Order entity itself yet.
        UUID restaurantIdPlaceholder = UUID.randomUUID(); // Placeholder - determine actual source

        Order order = new Order(
                cartDetails.getUserId(),
                restaurantIdPlaceholder, // Placeholder for restaurantId
                cartDetails.getCartId()
        );

        // 3. Add OrderItems to the Order
        for (OrderItem item : orderItems) {
            order.addOrderItem(item); // This also sets the bidirectional link and recalculates total
        }
        // order.calculateTotalAmount(); // addOrderItem should handle this

        // 4. Save the Order (which will also save OrderItems due to CascadeType.ALL)
        Order savedOrder = orderRepository.save(order);
        logger.info("Successfully created and saved order: {} with total amount: {}", savedOrder.getId(), savedOrder.getTotalAmount());

        // 5. TODO: Potentially publish an OrderCreatedEvent (for other services to react to)

        return savedOrder;
    }

    private Map<String, Integer> calculateItemQuantities(List<String> foodItemIds) {
        Map<String, Integer> quantities = new HashMap<>();
        if (foodItemIds == null) {
            return quantities;
        }
        for (String itemId : foodItemIds) {
            quantities.put(itemId, quantities.getOrDefault(itemId, 0) + 1);
        }
        return quantities;
    }
}