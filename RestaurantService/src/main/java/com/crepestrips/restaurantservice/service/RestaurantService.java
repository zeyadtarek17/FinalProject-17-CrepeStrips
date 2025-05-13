package com.crepestrips.restaurantservice.service;


import com.crepestrips.restaurantservice.dto.RestaurantOrderHistoryResponse;
import com.crepestrips.restaurantservice.factory.RestaurantFactory;
import com.crepestrips.restaurantservice.model.Category;
import com.crepestrips.restaurantservice.model.Restaurant;
import com.crepestrips.restaurantservice.repository.CategoryRepository;
import com.crepestrips.restaurantservice.repository.RestaurantRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RestaurantFactory restaurantFactory;

    public Restaurant create(Restaurant restaurant) {
//        Restaurant restaurant = new Restaurant();
//        Category category = restaurant.getCategory();
//        if (category != null) {
//            category = categoryRepository.save(category);
//        }
//        restaurant.setCategory(category);
//        restaurant.setName(dto.getName());
//        restaurant.setLocation(dto.getLocation());
//        restaurant.setOpeningTime(dto.getOpeningTime());
//        restaurant.setClosingTime(dto.getClosingTime());
////        restaurant.setHasSeating(dto.isHasSeating());
////        restaurant.setSupportsDelivery(dto.isSupportsDelivery());
////        restaurant.setRating(dto.getRating());
//        restaurant.setCategory(dto.getCategory());
//        restaurant.setCategoryID(dto.getCategoryId());
//        restaurant = restaurantFactory.createRestaurant(restaurant, dto.getType().name());
//        return restaurant;
        if (restaurant.getCategory() != null && restaurant.getCategory().getId() == null) {
            Category savedCategory = categoryRepository.save(restaurant.getCategory());
            restaurant.setCategory(savedCategory);  // Set the saved category with its ID
        }
        return repository.save(restaurant);
    }

    public List<Restaurant> getAll() {
        return repository.findAll();
    }

    public Optional<Restaurant> getById(String id) {
        return repository.findById(id);
    }


    public Optional<Restaurant> update(String id, Restaurant updated) {
        return repository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setLocation(updated.getLocation());
            existing.setRating(updated.getRating());
            existing.setOpen(isWithinOperatingHours(updated.getOpeningTime(), updated.getClosingTime()));
            existing.setOpeningTime(updated.getOpeningTime());
            existing.setClosingTime(updated.getClosingTime());
            existing.setFoodItemIds(updated.getFoodItemIds());
            existing.setFoodItem(updated.getFoodItem());
            existing.setType(updated.getType());
            existing.setCategory(updated.getCategory());
            return repository.save(existing);
        });
    }

    private boolean isWithinOperatingHours(LocalTime openingTime, LocalTime closingTime) {
        if (openingTime == null || closingTime == null) return false;
        LocalTime now = LocalTime.now();

        if (closingTime.isBefore(openingTime)) {
            return !now.isBefore(openingTime) || !now.isAfter(closingTime);
        }
        return !now.isBefore(openingTime) && !now.isAfter(closingTime);
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

    public List<Restaurant> getRestaurantsOpenNow() {
        LocalTime now = LocalTime.now();
        return repository.findAll().stream()
                .filter(restaurant -> {
                    LocalTime opening = restaurant.getOpeningTime();
                    LocalTime closing = restaurant.getClosingTime();
                    if (opening == null || closing == null) return false;

                    if (closing.isBefore(opening)) {
                        return !now.isBefore(opening) || !now.isAfter(closing);
                    }
                    return !now.isBefore(opening) && !now.isAfter(closing);
                })
                .collect(Collectors.toList());
    }


    public List<Restaurant> getAllRestaurants() {
        return repository.findAll();
    }
    public void banRestaurant(String restaurantId) {
        Restaurant restaurant = repository.findById(restaurantId).orElse(null);
        if (restaurant != null) {
           restaurant.setBanned(true);
           repository.save(restaurant);

        }
    }

//    @RabbitListener(queues = "restaurant.order.response.queue")
//    public void handleOrderHistoryResponse(RestaurantOrderHistoryResponse response) {
//        System.out.println("📦 Received orders for restaurant " + response.getRestaurantId());
//        response.getOrders().forEach(order ->
//                System.out.println("Order ID: " + order.getId() + " | Total: " + order.getTotalAmount())
//        );
//    }

}
