
package com.crepestrips.fooditemservice.service;

import com.crepestrips.fooditemservice.FoodItemFactory.DessertItem;
import com.crepestrips.fooditemservice.FoodItemFactory.FoodItemFactory;
import com.crepestrips.fooditemservice.FoodItemFactory.MainCourseItem;
import com.crepestrips.fooditemservice.client.RestaurantServiceClient;
import com.crepestrips.fooditemservice.model.FoodCategory;
import com.crepestrips.fooditemservice.model.FoodItem;
import com.crepestrips.fooditemservice.repository.FoodItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FoodItemService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FoodItemRepository repository;
    @Autowired
    private RestaurantServiceClient restaurantServiceClient;

    public List<FoodItem> getAll() {
        return repository.findAll();
    }

    public Optional<FoodItem> getById(String id) {
        return repository.findById(id);
    }

    public FoodItem create(Map<String, Object> rawData) {
        FoodItem base = objectMapper.convertValue(rawData, FoodItem.class);

        FoodItem fullItem = FoodItemFactory.createFoodItem(base.getCategory(), rawData, objectMapper);

        return repository.save(fullItem);
    }

    public FoodItem create(FoodItem item) {
        FoodItem created = FoodItemFactory.createFoodItem(item);
        return repository.save(created);
    }

    public Optional<FoodItem> update(String id, Map<String, Object> updates) {
        return repository.findById(id).map(existing -> {
            FoodCategory category = existing.getCategory();

            Map<String, Object> merged = objectMapper.convertValue(existing, Map.class);
            merged.putAll(updates); // updates override existing

            FoodItem updatedItem = switch (category) {
                case MAIN_COURSE -> objectMapper.convertValue(merged, MainCourseItem.class);
                case DESSERT -> objectMapper.convertValue(merged, DessertItem.class);
                default -> objectMapper.convertValue(merged, FoodItem.class);
            };

            updatedItem.setId(id);

            return repository.save(updatedItem);
        });
    }

    public boolean delete(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<FoodItem> applyDiscount(String id, double discountPercentage) {
        return repository.findById(id).map(item -> {
            item.setDiscount(discountPercentage);
            return repository.save(item);
        });
    }

    public List<FoodItem> getAvailableItems() {
        return repository.findByAvailableStockGreaterThan(0);
    }

    public List<FoodItem> getItemsSortedByRating() {
        return repository.findByOrderByRatingDesc();
    }

    public Optional<FoodItem> suspend(String id) {
        return repository.findById(id).map(item -> {
            item.setStatus("SUSPENDED");
            return repository.save(item);
        });
    }

    public Optional<FoodItem> unsuspend(String id) {
        return repository.findById(id).map(item -> {
            item.setStatus("UNSUSPENDED");
            return repository.save(item);
        });
    }

    public List<FoodItem> getFoodItemsByRestaurantId(String restaurantId) {
        return repository.findByRestaurantId(restaurantId);
    }

    public List<FoodItem> getItemsById(List<String> ids) {
        return repository.findAllById(ids);
    }

    public boolean decrementStock(List<String> ids) {
        // Step 1: Count how many times each ID appears
        Map<String, Long> itemCountMap = ids.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Step 2: Fetch all items from DB
        List<FoodItem> items = repository.findAllById(itemCountMap.keySet());

        // Step 3: Map food items by ID for quick lookup
        Map<String, FoodItem> itemMap = items.stream()
                .collect(Collectors.toMap(FoodItem::getId, Function.identity()));

        // Step 4: Check if each item has enough stock
        for (Map.Entry<String, Long> entry : itemCountMap.entrySet()) {
            FoodItem item = itemMap.get(entry.getKey());
            if (item == null || item.getAvailableStock() < entry.getValue()) {
                return false; // Not found or not enough stock
            }
        }

        // Step 5: All items have enough stock, now decrement
        for (Map.Entry<String, Long> entry : itemCountMap.entrySet()) {
            FoodItem item = itemMap.get(entry.getKey());
            item.setAvailableStock(item.getAvailableStock() - entry.getValue().intValue());
        }

        // Step 6: Save updated items
        repository.saveAll(itemMap.values());

        return true;
    }

    public List<String> removeSuspendedItems(List<String> ids) {
        // check if all items of same restaurant
        List<String> restaurantIds = ids.stream()
                .map(id -> repository.findById(id).orElse(null))
                .filter(item -> item != null)
                .map(FoodItem::getRestaurantId)
                .distinct()
                .collect(Collectors.toList());
        if (restaurantIds.size() > 1) {
            throw new IllegalArgumentException("All items must belong to the same restaurant");
        }

        // check if restaurant is open
        String restaurantId = restaurantIds.get(0);
        if (!restaurantServiceClient.isRestaurantAvailable(restaurantId)) {
            throw new IllegalArgumentException("Restaurant is closed");
        }

        //remove suspended items
        List<FoodItem> suspendedItems = repository.findAllById(ids).stream()
                .filter(item -> "SUSPENDED".equals(item.getStatus()))
                .collect(Collectors.toList());
        List<String> suspendedIds = suspendedItems.stream()
                .map(FoodItem::getId)
                .collect(Collectors.toList());

        // Remove suspended items from the original list
        ids.removeAll(suspendedIds);

        return ids;
    }

}
