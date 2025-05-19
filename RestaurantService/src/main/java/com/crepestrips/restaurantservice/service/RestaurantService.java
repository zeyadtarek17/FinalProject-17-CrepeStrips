package com.crepestrips.restaurantservice.service;


import com.crepestrips.restaurantservice.client.FoodItemClient;
import com.crepestrips.restaurantservice.client.OrderServiceClient;
import com.crepestrips.restaurantservice.dto.FoodItemDTO;
//import com.crepestrips.restaurantservice.dto.RestaurantOrderHistoryResponse;
import com.crepestrips.restaurantservice.dto.OrderResponseDto;
import com.crepestrips.restaurantservice.factory.RestaurantFactory;
import com.crepestrips.restaurantservice.model.Category;
import com.crepestrips.restaurantservice.model.Restaurant;
import com.crepestrips.restaurantservice.model.RestaurantCreation;
import com.crepestrips.restaurantservice.model.RestaurantType;
import com.crepestrips.restaurantservice.repository.CategoryRepository;
import com.crepestrips.restaurantservice.repository.RestaurantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

//    @Autowired
//    private RestaurantFactory restaurantFactory;

    @Autowired
    private OrderServiceClient orderServiceClient;

    @Autowired
    private ObjectMapper objectMapper;

    private final FoodItemClient foodItemClient;

    public RestaurantService(FoodItemClient foodItemClient) {
        this.foodItemClient = foodItemClient;
    }

    public Restaurant create(Map<String, Object> data) {
        RestaurantType type = objectMapper.convertValue(data.get("type"), RestaurantType.class);
        Restaurant specialized = RestaurantFactory.createRestaurant(type, data, objectMapper);
        return repository.save(specialized);
    }

    public List<Restaurant> getAll() {
        return repository.findAll();
    }

    public Optional<Restaurant> getById(String id) {
        return repository.findById(id);
    }


    public Optional<Restaurant> update(String id, Map<String, Object> updates) {
        return repository.findById(id).map(existing -> {
            Map<String, Object> merged = objectMapper.convertValue(existing, Map.class);
            merged.putAll(updates);
            RestaurantType type = objectMapper.convertValue(merged.get("type"), RestaurantType.class);
            Restaurant updated = RestaurantFactory.createRestaurant(type, merged, objectMapper);
            updated.setId(id);
            return repository.save(updated);
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

//    public List<Restaurant> getTopRatedRestaurants() {
//        return repository.findAllByOrderByRatingDesc();
//    }
    
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

    public boolean banRestaurant(String restaurantId) {
        Restaurant restaurant = repository.findById(restaurantId).orElse(null);
        if (restaurant != null) {
           restaurant.setBanned(true);
           repository.save(restaurant);
           return true;
        }
        return false;
    }
    public boolean addFoodItemIdToRestaurant(String restaurantId, String foodItemId) {
        Optional<Restaurant> optionalRestaurant = repository.findById(restaurantId);

        if (optionalRestaurant.isEmpty()) {
            return false; // restaurant not found
        }

        Restaurant restaurant = optionalRestaurant.get();
        List<String> foodItems = restaurant.getFoodItemIds();

        if (!foodItems.contains(foodItemId)) {
            foodItems.add(foodItemId);
            repository.save(restaurant);
        }

        return true;
    }

    public boolean unbanRestaurant(String id) {
        return repository.findById(id).map(restaurant -> {
            restaurant.setBanned(false);
            repository.save(restaurant);
            return true;
        }).orElse(false);
    }


    public List<OrderResponseDto> getOrderHistoryForRestaurant(String restaurantId) {
        return orderServiceClient.getOrdersByRestaurantId(restaurantId);
    }



//    @RabbitListener(queues = "restaurant.order.response.queue")
//    public void handleOrderHistoryResponse(RestaurantOrderHistoryResponse response) {
//        System.out.println("📦 Received orders for restaurant " + response.getRestaurantId());
//        response.getOrders().forEach(order ->
//                System.out.println("Order ID: " + order.getId() + " | Total: " + order.getTotalAmount())
//        );
//    }

}
