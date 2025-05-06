package com.crepestrips.restaurantservice.service;

import com.crepestrips.restaurantservice.model.Restaurant;
import com.crepestrips.restaurantservice.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository repository;


    public List<Restaurant> getAll() {
        return repository.findAll();
    }

    public Optional<Restaurant> getById(String id) {
        return repository.findById(id);
    }

    public Restaurant create(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    public Optional<Restaurant> update(String id, Restaurant updated) {
        return repository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setLocation(updated.getLocation());
            existing.setRating(updated.getRating());
            existing.setOpen(updated.isOpen());
            return repository.save(existing);
        });
    }

    public boolean delete(String id) {
        return repository.findById(id).map(existing -> {
            repository.deleteById(id);
            return true;
        }).orElse(false);
    }

    public List<Restaurant> getOpenRestaurants() {
        return repository.findByIsOpenTrue();
    }

    public List<Restaurant> getTopRatedRestaurants() {
        return repository.findAllByOrderByRatingDesc();
    }
    
//    public Restaurant getRestaurantById(String id) {
//        return repository.findById(id).get();
//    }

    public void addFoodItemToRestaurant(String restaurantId, String foodItemId) {
        Restaurant restaurant = repository.findById(restaurantId).orElse(null);
        if (restaurant != null) {
            List<String> foodItems = restaurant.getFoodItemIds();
            if (!foodItems.contains(foodItemId)) {
                foodItems.add(foodItemId);
                repository.save(restaurant);
            }
        }
    }

    public void removeFoodItemFromRestaurant(String restaurantId, String foodItemId) {
        Restaurant restaurant = repository.findById(restaurantId).orElse(null);
        if (restaurant != null) {
            List<String> foodItems = restaurant.getFoodItemIds();
            foodItems.remove(foodItemId);
            repository.save(restaurant);
        }
    }

    public void updateFoodItemInRestaurant(String restaurantId, String oldFoodItemId, String newFoodItemId) {
        Restaurant restaurant = repository.findById(restaurantId).orElse(null);
        if (restaurant != null) {
            List<String> foodItems = restaurant.getFoodItemIds();
            int index = foodItems.indexOf(oldFoodItemId);
            if (index != -1) {
                foodItems.set(index, newFoodItemId);
                repository.save(restaurant);
            }
        }
    }


}
