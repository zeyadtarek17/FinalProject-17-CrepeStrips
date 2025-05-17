package com.crepestrips.restaurantservice.controller;

import com.crepestrips.restaurantservice.client.FoodItemClient;
import com.crepestrips.restaurantservice.config.FoodItemMessage;
import com.crepestrips.restaurantservice.config.RestaurantProducer;
import com.crepestrips.restaurantservice.dto.FoodItemDTO;
import com.crepestrips.restaurantservice.factory.RestaurantFactory;
import com.crepestrips.restaurantservice.model.Category;
import com.crepestrips.restaurantservice.model.RestaurantCreation;
import com.crepestrips.restaurantservice.repository.CategoryRepository;
import com.crepestrips.restaurantservice.repository.RestaurantRepository;
import com.crepestrips.restaurantservice.strategy.FilterFactory;
import com.crepestrips.restaurantservice.strategy.FilterStrategy;
import com.crepestrips.restaurantservice.model.Restaurant;
import com.crepestrips.restaurantservice.service.RestaurantService;
import com.crepestrips.restaurantservice.strategy.RestaurantFilterContext;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService service;
    @Autowired
    private RestaurantFilterContext context;
//    @Autowired
//    private RestaurantFactory restaurantFactory;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private final RestaurantProducer restaurantProducer;
    private final FoodItemClient foodItemClient;


    public RestaurantController(RestaurantProducer restaurantProducer, FoodItemClient foodItemClient) {
        this.restaurantProducer = restaurantProducer;
        this.foodItemClient = foodItemClient;
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAll() {
        List<Restaurant> restaurants = service.getAll();

        LocalTime now = LocalTime.now();

        for (Restaurant r : restaurants) {
            if (r.getOpeningTime() != null && r.getClosingTime() != null) {
                boolean isOpen = !now.isBefore(r.getOpeningTime()) && !now.isAfter(r.getClosingTime());
                r.setOpen(isOpen);
            } else {
                r.setOpen(false);
            }
        }

        return ResponseEntity.ok(restaurants);    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getById(@PathVariable String id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Map<String, Object> requestData) {
        Restaurant created = service.create(requestData);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable String id, @RequestBody Map<String, Object> requestData) {
        return service.update(id, requestData)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


//    @PostMapping
//    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Map<String, Object> requestData) {
//        Restaurant created = service.create(requestData);
//        return ResponseEntity.status(HttpStatus.CREATED).body(created);
//    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable String id, @RequestBody Map<String, Object> requestData) {
//        return service.update(id, requestData)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/open")
    public ResponseEntity<List<Restaurant>> getOpenRestaurants() {
        return ResponseEntity.ok(service.getOpenRestaurants());
    }

//    @GetMapping("/top-rated")
//    public ResponseEntity<List<Restaurant>> getTopRatedRestaurants() {
//        return ResponseEntity.ok(service.getTopRatedRestaurants());
//    }

    @PutMapping("/{restaurantId}/addFoodItem/{foodItemId}")
    public ResponseEntity<String> addFoodItemToRestaurant(@PathVariable String restaurantId, @PathVariable String foodItemId) {
        service.addFoodItemToRestaurant(restaurantId, foodItemId);
        return ResponseEntity.ok("Food item added to restaurant");
    }

    @PutMapping("/{restaurantId}/removeFoodItem/{foodItemId}")
    public ResponseEntity<String> removeFoodItemFromRestaurant(@PathVariable String restaurantId, @PathVariable String foodItemId) {
        service.removeFoodItemFromRestaurant(restaurantId, foodItemId);
        return ResponseEntity.ok("Food item removed from restaurant");
    }

    @PutMapping("/{restaurantId}/updateFoodItem")
    public ResponseEntity<String> updateFoodItemInRestaurant(
            @PathVariable String restaurantId,
            @RequestParam String oldFoodItemId,
            @RequestParam String newFoodItemId) {
        service.updateFoodItemInRestaurant(restaurantId, oldFoodItemId, newFoodItemId);
        return ResponseEntity.ok("Food item updated in restaurant");
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
        System.out.println("filterType: " + filterType + ", criteria: " + criteria);

        return context.applyFilter(filterType, allRestaurants, criteria);
    }
    @PostMapping("/add-food-item")
    public ResponseEntity<String> addFoodItemToRestaurant(@RequestBody FoodItemDTO dto) {
        restaurantProducer.sendNewFoodItem(dto);
        return ResponseEntity.ok("Food item sent asynchronously!");
    }
    @PutMapping("/update-food-item/{id}")
    public ResponseEntity<String> updateFoodItemAsync(
            @PathVariable String id,
            @RequestBody FoodItemDTO dto) {
        FoodItemMessage message = new FoodItemMessage();
        message.setAction("UPDATE");
        message.setFoodItemId(id);
        message.setPayload(dto);
        restaurantProducer.sendFoodItemCommand(message);
        return ResponseEntity.ok("Update message sent!");
    }

    @DeleteMapping("/delete-food-item/{id}")
    public ResponseEntity<String> deleteFoodItemAsync(@PathVariable String id) {
        FoodItemMessage message = new FoodItemMessage();
        message.setAction("DELETE");
        message.setFoodItemId(id);
        restaurantProducer.sendFoodItemCommand(message);
        return ResponseEntity.ok("Delete message sent!");
    }

    @PostMapping("/{restaurantId}/order-history-request")
    public ResponseEntity<String> fetchOrderHistory(@PathVariable String restaurantId) {
        restaurantProducer.sendOrderHistoryRequest(restaurantId);
        return ResponseEntity.ok("Order history request sent via RabbitMQ.");
    }
    @PostMapping("/{restaurantId}/fooditems")
    public ResponseEntity<FoodItemDTO> createFoodItem( @PathVariable String restaurantId, @RequestBody FoodItemDTO dto) {

        FoodItemDTO created = foodItemClient.createFoodItem(dto);
        service.addFoodItemToRestaurant(restaurantId, created.getId());
        return ResponseEntity.ok(created);
    }
    @PutMapping("/{restaurantId}/fooditems/{foodItemId}")
    public ResponseEntity<FoodItemDTO> updateFoodItemSync(
            @PathVariable String restaurantId,
            @PathVariable String foodItemId,
            @RequestBody FoodItemDTO dto) {

        FoodItemDTO updated = foodItemClient.updateFoodItem(foodItemId, dto);


        service.updateFoodItemInRestaurant(restaurantId, foodItemId, updated.getId());

        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/{restaurantId}/fooditems/{foodItemId}")
    public ResponseEntity<String> deleteFoodItemSync(
            @PathVariable String restaurantId,
            @PathVariable String foodItemId) {

        foodItemClient.deleteFoodItem(foodItemId);
        service.removeFoodItemFromRestaurant(restaurantId, foodItemId);

        return ResponseEntity.ok("Food item deleted successfully and unlinked from restaurant.");
    }

    @PutMapping("/{id}/ban")
    public ResponseEntity<String> banRestaurant(@PathVariable String id) {
        boolean success = service.banRestaurant(id);
        if (success) {
            return ResponseEntity.ok("Restaurant has been banned.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/unban")
    public ResponseEntity<String> unbanRestaurant(@PathVariable String id) {
        boolean success = service.unbanRestaurant(id);
        return success
                ? ResponseEntity.ok("Restaurant has been unbanned.")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurant not found.");
    }

}
