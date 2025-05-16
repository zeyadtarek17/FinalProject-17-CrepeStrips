
package com.crepestrips.fooditemservice.service;

import com.crepestrips.fooditemservice.FoodItemFactory.DessertItem;
import com.crepestrips.fooditemservice.FoodItemFactory.FoodItemFactory;
import com.crepestrips.fooditemservice.FoodItemFactory.MainCourseItem;
import com.crepestrips.fooditemservice.model.FoodCategory;
import com.crepestrips.fooditemservice.model.FoodItem;
import com.crepestrips.fooditemservice.repository.FoodItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FoodItemService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FoodItemRepository repository;

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

}
