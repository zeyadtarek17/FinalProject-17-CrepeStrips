package com.crepestrips.fooditemservice.controller;

import com.crepestrips.fooditemservice.dto.FoodItemDTO;
import com.crepestrips.fooditemservice.model.FoodItem;
import com.crepestrips.fooditemservice.service.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<FoodItem> create(@RequestBody FoodItem item) {
        return ResponseEntity.ok(service.create(item));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodItem> update(@PathVariable String id, @RequestBody FoodItem item) {
        return service.update(id, item)
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



}
