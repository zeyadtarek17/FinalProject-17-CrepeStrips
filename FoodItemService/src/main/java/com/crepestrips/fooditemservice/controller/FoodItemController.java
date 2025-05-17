package com.crepestrips.fooditemservice.controller;

import com.crepestrips.fooditemservice.dto.DefaultResult;
import com.crepestrips.fooditemservice.model.FoodItem;
import com.crepestrips.fooditemservice.service.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/fooditems")
public class FoodItemController {

    @Autowired
    private FoodItemService service;

    @GetMapping
    public ResponseEntity<DefaultResult> getAll() {
        List<FoodItem> items = service.getAll();
        return ResponseEntity.ok(new DefaultResult("All food items retrieved", false, items));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DefaultResult> getById(@PathVariable String id) {
        Optional<FoodItem> item = service.getById(id);
        if (item.isPresent()) {
            return ResponseEntity.ok(new DefaultResult("Food item found", false, item.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new DefaultResult("Food item not found", true, null));
    }

    @PostMapping
    public ResponseEntity<DefaultResult> create(@RequestBody Map<String, Object> rawData) {
        FoodItem created = service.create(rawData);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new DefaultResult("Food item created", false, created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DefaultResult> update(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        Optional<FoodItem> updated = service.update(id, updates);
        return updated.map(item -> ResponseEntity.ok(new DefaultResult("Food item updated", false, item)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new DefaultResult("Food item not found", true, null)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DefaultResult> delete(@PathVariable String id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity
                    .ok(new DefaultResult("Item with ID " + id + " was successfully deleted.", false, null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new DefaultResult("Item with ID " + id + " not found.", true, null));
        }
    }

    @PatchMapping("/{id}/discount")
    public ResponseEntity<DefaultResult> applyDiscount(@PathVariable String id, @RequestParam double discount) {
        Optional<FoodItem> updated = service.applyDiscount(id, discount);
        return updated.map(item -> ResponseEntity.ok(new DefaultResult("Discount applied", false, item)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new DefaultResult("Food item not found", true, null)));
    }

    @GetMapping("/available")
    public ResponseEntity<DefaultResult> getAvailableItems() {
        return ResponseEntity.ok(new DefaultResult("Available items retrieved", false, service.getAvailableItems()));
    }

    @GetMapping("/top-rated")
    public ResponseEntity<DefaultResult> getTopRatedItems() {
        return ResponseEntity
                .ok(new DefaultResult("Top rated items retrieved", false, service.getItemsSortedByRating()));
    }

    @PutMapping("/{id}/suspend")
    public ResponseEntity<FoodItem> suspend(@PathVariable String id) {
        try {
            return service.suspend(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            e.printStackTrace(); // Optional: log the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/unsuspend")
    public ResponseEntity<FoodItem> unsuspend(@PathVariable String id) {
        try {
            return service.unsuspend(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            e.printStackTrace(); // Optional: log the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getAllFoodItems/{restaurantId}")
    public ResponseEntity<DefaultResult> getByRestaurant(@PathVariable String restaurantId) {
        List<FoodItem> items = service.getFoodItemsByRestaurantId(restaurantId);
        return ResponseEntity.ok(new DefaultResult("Items for restaurant retrieved", false, items));
    }

    @PostMapping("/all")
    public ResponseEntity<DefaultResult> getItemsById(@RequestBody List<String> ids) {
        List<FoodItem> items = service.getItemsById(ids);
        return ResponseEntity.ok(new DefaultResult("Items retrieved by ID list", false, items));
    }

    @PostMapping("/decrement")
    public ResponseEntity<DefaultResult> decrementStock(@RequestBody List<String> ids) {
        boolean success = service.decrementStock(ids);
        if (success) {
            return ResponseEntity.ok(new DefaultResult("Stock decremented", false, null));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new DefaultResult("Failed to decrement stock", true, null));
        }
    }

}
