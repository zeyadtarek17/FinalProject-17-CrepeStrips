package com.crepestrips.restaurantservice.controller;

import com.crepestrips.restaurantservice.dto.FoodItemDTO;
import com.crepestrips.restaurantservice.dto.RestaurantDto;
import com.crepestrips.restaurantservice.factory.RestaurantFactory;
import com.crepestrips.restaurantservice.repository.RestaurantRepository;
import com.crepestrips.restaurantservice.strategy.FilterFactory;
import com.crepestrips.restaurantservice.strategy.FilterStrategy;
import com.crepestrips.restaurantservice.model.Restaurant;
import com.crepestrips.restaurantservice.service.RestaurantService;
import com.crepestrips.restaurantservice.strategy.RestaurantFilterContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService service;
    @Autowired
    private RestaurantFilterContext context;
    @Autowired
    private RestaurantFactory restaurantFactory;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getById(@PathVariable String id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody RestaurantDto dto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(dto.getName());
        restaurant.setLocation(dto.getLocation());

        // Apply factory behavior
        restaurant = restaurantFactory.createRestaurant(restaurant, dto.getType());

        return ResponseEntity.ok(restaurantRepository.save(restaurant));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> update(@PathVariable String id, @RequestBody Restaurant restaurant) {
        return service.update(id, restaurant)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/open")
    public ResponseEntity<List<Restaurant>> getOpenRestaurants() {
        return ResponseEntity.ok(service.getOpenRestaurants());
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<Restaurant>> getTopRatedRestaurants() {
        return ResponseEntity.ok(service.getTopRatedRestaurants());
    }

    @PutMapping("/{restaurantId}/addFoodItem/{foodItemId}")
    public void addFoodItemToRestaurant(@PathVariable String restaurantId, @PathVariable String foodItemId) {
        service.addFoodItemToRestaurant(restaurantId, foodItemId);
    }

    @PutMapping("/{restaurantId}/removeFoodItem/{foodItemId}")
    public void removeFoodItemFromRestaurant(@PathVariable String restaurantId, @PathVariable String foodItemId) {
        service.removeFoodItemFromRestaurant(restaurantId, foodItemId);
    }

    @PutMapping("/{restaurantId}/updateFoodItem")
    public void updateFoodItemInRestaurant(
            @PathVariable String restaurantId,
            @RequestParam String oldFoodItemId,
            @RequestParam String newFoodItemId) {
        service.updateFoodItemInRestaurant(restaurantId, oldFoodItemId, newFoodItemId);
    }

//    @GetMapping("/filter/by-hours")
//    public ResponseEntity<List<Restaurant>> filterByOperatingHours(
//            @RequestParam String from,
//            @RequestParam String to) {
//
//        LocalTime fromTime = LocalTime.parse(from);
//        LocalTime toTime = LocalTime.parse(to);
//        String criteria = Arrays.toString(new LocalTime[] { fromTime, toTime });
//
//        List<Restaurant> allRestaurants = service.getAll();
//        FilterStrategy strategy = FilterFactory.getFilter("hours");
//        List<Restaurant> filtered = strategy.filter(allRestaurants, criteria);
//
//        return ResponseEntity.ok(filtered);
//    }
    @GetMapping("/filter")
    public List<Restaurant> filterRestaurants(
            @RequestParam String filterType,
            @RequestParam String criteria) {

        List<Restaurant> allRestaurants = service.getAllRestaurants();
        return context.applyFilter(filterType, allRestaurants, criteria);
    }

}
