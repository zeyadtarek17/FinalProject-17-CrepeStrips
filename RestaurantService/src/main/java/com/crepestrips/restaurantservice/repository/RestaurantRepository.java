package com.crepestrips.restaurantservice.repository;

import com.crepestrips.restaurantservice.model.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
    List<Restaurant> findByIsOpenTrue();
    List<Restaurant> findAllByOrderByRatingDesc();
    List<Restaurant> findByType(String type);
}
