package com.crepestrips.restaurantservice.controller;

import com.crepestrips.restaurantservice.factories.FilterFactory;
import com.crepestrips.restaurantservice.factories.FilterStrategy;
import com.crepestrips.restaurantservice.model.Restaurant;
import com.crepestrips.restaurantservice.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService service;

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
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant restaurant) {
        return ResponseEntity.ok(service.create(restaurant));
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

    @GetMapping("/filter/by-hours")
    public ResponseEntity<List<Restaurant>> filterByOperatingHours(
            @RequestParam String from,
            @RequestParam String to) {

        LocalTime fromTime = LocalTime.parse(from);
        LocalTime toTime = LocalTime.parse(to);
        LocalTime[] criteria = new LocalTime[] { fromTime, toTime };

        List<Restaurant> allRestaurants = service.getAll();
        FilterStrategy strategy = FilterFactory.getFilter("hours");
        List<Restaurant> filtered = strategy.filter(allRestaurants, criteria);

        return ResponseEntity.ok(filtered);
    }
}
