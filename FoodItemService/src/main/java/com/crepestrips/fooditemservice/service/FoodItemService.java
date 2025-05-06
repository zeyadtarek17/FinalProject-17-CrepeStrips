
package com.crepestrips.fooditemservice.service;

import com.crepestrips.fooditemservice.FoodItemFactory.FoodItemFactory;
import com.crepestrips.fooditemservice.model.FoodItem;
import com.crepestrips.fooditemservice.repository.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
}
