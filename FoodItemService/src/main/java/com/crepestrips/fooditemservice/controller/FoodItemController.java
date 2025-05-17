package com.crepestrips.fooditemservice.controller;

import com.crepestrips.fooditemservice.dto.FoodItemDTO;
import com.crepestrips.fooditemservice.model.FoodItem;
import com.crepestrips.fooditemservice.service.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fooditems")
public class FoodItemController {

    @Autowired
    private FoodItemService service;

    @GetMapping
    public ResponseEntity<List<FoodItem>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodItem> getById(@PathVariable String id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FoodItem> create(@RequestBody Map<String, Object> rawData) {
        return ResponseEntity.ok(service.create(rawData));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodItem> update(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        return service.update(id, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/discount")
    public ResponseEntity<FoodItem> applyDiscount(@PathVariable String id, @RequestParam double discount) {
        return service.applyDiscount(id, discount)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/available")
    public ResponseEntity<List<FoodItem>> getAvailableItems() {
        return ResponseEntity.ok(service.getAvailableItems());
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<FoodItem>> getTopRatedItems() {
        return ResponseEntity.ok(service.getItemsSortedByRating());
    }

    @PutMapping("/{id}/suspend")
    public ResponseEntity<FoodItem> suspend(@PathVariable String id) {
        return service.suspend(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/all")
    public ResponseEntity<List<FoodItem>> getItemsById(@RequestBody List<String> ids) {
        List<FoodItem> items = service.getItemsById(ids);
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{id}/unsuspend")
    public ResponseEntity<FoodItem> unsuspend(@PathVariable String id) {
        return service.unsuspend(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<FoodItem>> getByRestaurant(@PathVariable String restaurantId) {
        return ResponseEntity.ok(service.getFoodItemsByRestaurantId(restaurantId));
    }
    @PostMapping("/decrement")
    public ResponseEntity<Boolean> decrementStock(@RequestBody List<String> ids) {
        return ResponseEntity.ok(service.decrementStock(ids));
    }

}
