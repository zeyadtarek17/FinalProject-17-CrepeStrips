
package com.crepestrips.fooditemservice.service;

import com.crepestrips.fooditemservice.FoodItemFactory.FoodItemFactory;
import com.crepestrips.fooditemservice.model.FoodItem;
import com.crepestrips.fooditemservice.repository.FoodItemRepository;
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
    private FoodItemRepository repository;

    public List<FoodItem> getAll() {
        return repository.findAll();
    }

    public Optional<FoodItem> getById(String id) {
        return repository.findById(id);
    }

    public FoodItem create(FoodItem item) {
        FoodItem created = FoodItemFactory.createFoodItem(item);
        return repository.save(created);
    }

    public Optional<FoodItem> update(String id, FoodItem updatedItem) {
        return repository.findById(id).map(existing -> {
            existing.setName(updatedItem.getName());
            existing.setDescription(updatedItem.getDescription());
            existing.setPrice(updatedItem.getPrice());
            existing.setDiscount(updatedItem.getDiscount());
            existing.setRating(updatedItem.getRating());
            existing.setAvailableStock(updatedItem.getAvailableStock());
            existing.setCategory(updatedItem.getCategory());
            existing.setRestaurantId(updatedItem.getRestaurantId());
            return repository.save(existing);
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

}
