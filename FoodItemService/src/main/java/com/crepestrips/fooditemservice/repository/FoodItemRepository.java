package com.crepestrips.fooditemservice.repository;

import com.crepestrips.fooditemservice.model.FoodItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface FoodItemRepository extends MongoRepository<FoodItem, String> {

    List<FoodItem> findByAvailableStockGreaterThan(int stockThreshold);

    List<FoodItem> findByOrderByRatingDesc();
}
