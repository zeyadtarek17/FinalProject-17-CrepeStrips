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
        logger.info("OrderCreationService: Starting to create order for cartId: {}", cartDetails.getCartId());

        Map<String, Integer> itemQuantities = calculateItemQuantities(cartDetails.getFoodItemIds());
        logger.info("Calculated item quantities: {}", itemQuantities);

        List<OrderItem> orderItems = new ArrayList<>();
        String orderLevelRestaurantId = null;
        List<String> allFoodItemIdsForStockDecrement = new ArrayList<>();
        boolean proceedWithOrderCreation = true; // Flag to control if we should create the order

        if (itemQuantities.isEmpty()) {
            logger.warn("No items found in cart {}. Order creation will be skipped.", cartDetails.getCartId());
            return null; // Or handle as per business rule for empty carts
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
                    String errorMessage = responseWrapper != null ? responseWrapper.getMessage() : "null response";
                    logger.warn("Received error or no result from FoodItemService for ID {}: {}. Skipping item.", foodItemIdStringFromCart, errorMessage);
                    continue;
                }
            } catch (Exception e) {
                logger.error("Error calling FoodItemService for food item ID {}: {}. Skipping this item.", foodItemIdStringFromCart, e.getMessage(), e);
                continue;
            }

            if (foodItemInfo.getId() == null) {
                logger.warn("FoodItemService returned item with null ID for cart item ID string {}. Skipping item.", foodItemIdStringFromCart);
                continue;
            }

            // --- Logic for Restaurant ID ---
            if (foodItemInfo.getRestaurantId() != null && !foodItemInfo.getRestaurantId().isBlank()) {
                if (orderLevelRestaurantId == null) {
                    orderLevelRestaurantId = foodItemInfo.getRestaurantId();
                    logger.info("Derived order-level restaurantId: {} from food item: {}", orderLevelRestaurantId, foodItemInfo.getId());
                } else if (!orderLevelRestaurantId.equals(foodItemInfo.getRestaurantId())) {
                    logger.error("CRITICAL: Items from different restaurants found in the same cart! Cart ID: {}. Expected restaurant: {}, Found: {} for item: {}. Order creation will be aborted.",
                            cartDetails.getCartId(), orderLevelRestaurantId, foodItemInfo.getRestaurantId(), foodItemInfo.getId());
                    proceedWithOrderCreation = false; // Mark to abort order creation
                    break; // Stop processing further items as this is a fundamental issue
                }
            } else {
                logger.warn("Food item {} from FoodItemService has a null or blank restaurantId. This might be an issue if a restaurantId is mandatory for the order.", foodItemInfo.getId());
                // If restaurantId is absolutely mandatory per item for an order to be valid,
                // you might set proceedWithOrderCreation = false; and break; here too.
                // For now, we'll allow it and see if an overall orderLevelRestaurantId can be determined.
            }
            // --- End of Logic for Restaurant ID ---

            double priceForOrderItem;
            if (foodItemInfo.getPrice() != null) {
                priceForOrderItem = foodItemInfo.getPrice();
            } else {
                priceForOrderItem = 0.0;
                logger.warn("FoodItemDTO for id {} has null price. Defaulting to 0.0.", foodItemInfo.getId());
            }

            logger.info("Processing item: ID={}, Name={}, Price={}, Quantity={}, RestaurantID={}",
                        foodItemInfo.getId(), foodItemInfo.getName(), priceForOrderItem, quantity, foodItemInfo.getRestaurantId());

            OrderItem orderItem = new OrderItem(
                    foodItemInfo.getId(),
                    foodItemInfo.getName(),
                    quantity,
                    priceForOrderItem
            );
            orderItems.add(orderItem);

            for (int i = 0; i < quantity; i++) {
                allFoodItemIdsForStockDecrement.add(foodItemInfo.getId());
            }
        }

        if (!proceedWithOrderCreation) {
            logger.error("Order creation aborted due to critical issues (e.g., mismatched restaurant IDs). Cart ID: {}", cartDetails.getCartId());
            return null; // Indicate failure to the listener
        }

        if (orderItems.isEmpty() && !itemQuantities.isEmpty()) {
            logger.error("No valid order items could be created after fetching from FoodItemService for cart {}. Aborting order creation.", cartDetails.getCartId());
            // This is a fundamental failure if there were items in the cart but none could be processed.
            // Returning null will signal the listener that order creation failed.
            return null;
        }

        // If after processing all items, orderLevelRestaurantId is still null,
        // and there are items, this might be an error condition.
        if (orderLevelRestaurantId == null && !orderItems.isEmpty()) {
            logger.error("Could not determine a restaurantId for order from any of its items. Cart ID: {}. Order creation will be aborted.", cartDetails.getCartId());
            // Depending on business rules, you might allow orders without a restaurant ID,
            // or this could be a hard failure. For now, let's treat it as a failure.
            return null;
        }

        Order order = new Order(
                cartDetails.getUserId(),
                orderLevelRestaurantId, // This might be null if no items had a restaurantId and we decided to allow it
                cartDetails.getCartId()
        );

        for (OrderItem item : orderItems) {
            order.addOrderItem(item);
        }

        Order savedOrder = orderRepository.save(order);
        logger.info("OrderCreationService: Successfully created and saved order {} for restaurant {} with total amount: {}",
                savedOrder.getId(), savedOrder.getRestaurantId(), savedOrder.getTotalAmount());

        // --- Decrement Stock AFTER order is successfully saved ---
        if (!allFoodItemIdsForStockDecrement.isEmpty()) {
            try {
                logger.info("Attempting to decrement stock for order {}. Item IDs to decrement (repeated for quantity): {}",
                            savedOrder.getId(), allFoodItemIdsForStockDecrement);
                ResponseEntity<DefaultResult> stockDecrementResponse = foodItemServiceClient.decrementStock(allFoodItemIdsForStockDecrement);

                if (stockDecrementResponse.getStatusCode().is2xxSuccessful() &&
                    stockDecrementResponse.getBody() != null &&
                    !stockDecrementResponse.getBody().isError()) {
                    logger.info("Successfully decremented stock for order {}. Response message: {}",
                                savedOrder.getId(), stockDecrementResponse.getBody().getMessage());
                } else {
                    String errorMessage = "Unknown error during stock decrement.";
                    if (stockDecrementResponse.getBody() != null && stockDecrementResponse.getBody().getMessage() != null) {
                        errorMessage = stockDecrementResponse.getBody().getMessage();
                    } else if (!stockDecrementResponse.getStatusCode().is2xxSuccessful()){
                        errorMessage = "Received non-2xx status: " + stockDecrementResponse.getStatusCode();
                    }
                    logger.error("CRITICAL: Failed to decrement stock for order {} after creation. Details: {}",
                            savedOrder.getId(), errorMessage);
                    // In a real system, this requires a compensation action (e.g., publish StockDecrementFailedEvent)
                }
            } catch (Exception e) {
                logger.error("CRITICAL: Exception occurred while trying to decrement stock for order {} after creation: {}",
                        savedOrder.getId(), e.getMessage(), e);
                // Compensation needed here too.
            }
        } else {
            // This case can happen if orderItems was empty but an order was still created (if business rules allow)
            // OR if allFoodItemIdsForStockDecrement ended up empty due to all items being skipped.
            logger.info("No items to decrement stock for order {} (perhaps cart was empty or all items failed lookup/processing)", savedOrder.getId());
        }
        // --- End of Stock Decrement ---

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