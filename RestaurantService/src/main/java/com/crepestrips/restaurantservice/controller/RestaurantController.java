package com.crepestrips.restaurantservice.controller;

import com.crepestrips.restaurantservice.client.FoodItemClient;
import com.crepestrips.restaurantservice.config.FoodItemMessage;
import com.crepestrips.restaurantservice.config.RestaurantProducer;
import com.crepestrips.restaurantservice.dto.DefaultResult;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final RestaurantProducer restaurantProducer;
    private final FoodItemClient foodItemClient;


    public RestaurantController(RestaurantProducer restaurantProducer, FoodItemClient foodItemClient) {
        this.restaurantProducer = restaurantProducer;
        this.foodItemClient = foodItemClient;
    }

    @GetMapping
    public ResponseEntity<DefaultResult> getAll() {
        try {
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
            return ResponseEntity.ok(new DefaultResult("Restaurants retrieved", false, restaurants));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<DefaultResult> getById(@PathVariable String id) {
        try {
            return service.getById(id)
                    .map(restaurant -> ResponseEntity.ok(new DefaultResult("Restaurant found", false, restaurant)))
                    .orElse(ResponseEntity.ok(new DefaultResult("Restaurant not found", true, null)));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }
    @PostMapping
    public ResponseEntity<DefaultResult> createRestaurant(@RequestBody RestaurantCreation restaurantCreation) {
        try {
            Restaurant created = service.create(restaurantCreation);
            return ResponseEntity.status(HttpStatus.CREATED).body(new DefaultResult("Restaurant created", false, created));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<DefaultResult> update(@PathVariable String id, @RequestBody Restaurant restaurant) {
        try {
            return service.update(id, restaurant)
                    .map(updated -> ResponseEntity.ok(new DefaultResult("Restaurant updated", false, updated)))
                    .orElse(ResponseEntity.ok(new DefaultResult("Restaurant not found", true, null)));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<DefaultResult> delete(@PathVariable String id) {
        try {
            boolean deleted = service.delete(id);
            return deleted
                    ? ResponseEntity.ok(new DefaultResult("Restaurant deleted", false, null))
                    : ResponseEntity.ok(new DefaultResult("Restaurant not found", true, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @GetMapping("/open")
    public ResponseEntity<DefaultResult> getOpenRestaurants() {
        try {
            List<Restaurant> openRestaurants = service.getOpenRestaurants();
            return ResponseEntity.ok(new DefaultResult("Open restaurants retrieved", false, openRestaurants));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

//    @GetMapping("/top-rated")
//    public ResponseEntity<List<Restaurant>> getTopRatedRestaurants() {
//        return ResponseEntity.ok(service.getTopRatedRestaurants());
//    }


//    @GetMapping("/filter/by-hours")
//    public ResponseEntity<List<Restaurant>> filterByOperatingHours(
//            @RequestParam String from,
//            @RequestParam String to) {
//
//        LocalTime fromTime = LocalTime.parse(from);
//        LocalTime toTime = LocalTime.parse(to);
//        String criteria = Arrays.toString(new
//
//        LocalTime[] { fromTime, toTime });
//
//        List<Restaurant> allRestaurants = service.getAll();
//        FilterStrategy strategy = FilterFactory.getFilter("hours");
//        List<Restaurant> filtered = strategy.filter(allRestaurants, criteria);
//
//        return ResponseEntity.ok(filtered);
//    }
    @GetMapping("/filter")
    public ResponseEntity<DefaultResult> filterRestaurants(@RequestParam String filterType, @RequestParam String criteria) {
        try {
            List<Restaurant> allRestaurants = service.getAllRestaurants();
            List<Restaurant> filtered = context.applyFilter(filterType, allRestaurants, criteria);
            return ResponseEntity.ok(new DefaultResult("Filtered restaurants retrieved", false, filtered));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }
    @PostMapping("/add-food-item")
    public ResponseEntity<DefaultResult> addFoodItemToRestaurant(@RequestBody FoodItemDTO dto) {
        try {
            restaurantProducer.sendNewFoodItem(dto);
            return ResponseEntity.ok(new DefaultResult("Food item sent asynchronously", false, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }
    @PutMapping("/update-food-item/{id}")
    public ResponseEntity<DefaultResult> updateFoodItemAsync(@PathVariable String id, @RequestBody FoodItemDTO dto) {
        try {
            FoodItemMessage message = new FoodItemMessage();
            message.setAction("UPDATE");
            message.setFoodItemId(id);
            message.setPayload(dto);
            restaurantProducer.sendFoodItemCommand(message);
            return ResponseEntity.ok(new DefaultResult("Update message sent", false, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }


    @DeleteMapping("/delete-food-item/{id}")
    public ResponseEntity<DefaultResult> deleteFoodItemAsync(@PathVariable String id) {
        try {
            FoodItemMessage message = new FoodItemMessage();
            message.setAction("DELETE");
            message.setFoodItemId(id);
            restaurantProducer.sendFoodItemCommand(message);
            return ResponseEntity.ok(new DefaultResult("Delete message sent", false, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }


    @PostMapping("/{restaurantId}/order-history-request")
    public ResponseEntity<DefaultResult> fetchOrderHistory(@PathVariable String restaurantId) {
        try {
            restaurantProducer.sendOrderHistoryRequest(restaurantId);
            return ResponseEntity.ok(new DefaultResult("Order history request sent", false, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }
    @PostMapping("/{restaurantId}/fooditems")
    public ResponseEntity<DefaultResult> createFoodItem(@PathVariable String restaurantId, @RequestBody FoodItemDTO dto) {
        try {
            DefaultResult result = foodItemClient.createFoodItem(dto);
            if (result.isError() || result.getResult() == null) {
                return ResponseEntity.ok(new DefaultResult("Failed to create food item", true, null));
            }
            ObjectMapper mapper = new ObjectMapper();
            FoodItemDTO created = mapper.convertValue(result.getResult(), FoodItemDTO.class);
            service.addFoodItemToRestaurant(restaurantId, created.getId());
            return ResponseEntity.ok(new DefaultResult("Food item created and added to restaurant", false, created));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }
    @PutMapping("/{restaurantId}/fooditems/{foodItemId}")
    public ResponseEntity<DefaultResult> updateFoodItemSync(@PathVariable String restaurantId, @PathVariable String foodItemId, @RequestBody FoodItemDTO dto) {
        try {
            DefaultResult result = foodItemClient.updateFoodItem(foodItemId, dto);
            if (result.isError()) {
                return ResponseEntity.ok(new DefaultResult("Failed to update food item", true, null));
            }
            FoodItemDTO updated = (FoodItemDTO) result.getResult();
            service.updateFoodItemInRestaurant(restaurantId, foodItemId, updated.getId());
            return ResponseEntity.ok(new DefaultResult("Food item updated in restaurant", false, updated));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }
    @DeleteMapping("/{restaurantId}/fooditems/{foodItemId}")
    public ResponseEntity<DefaultResult> deleteFoodItemSync(@PathVariable String restaurantId, @PathVariable String foodItemId) {
        try {
            foodItemClient.deleteFoodItem(foodItemId);
            service.removeFoodItemFromRestaurant(restaurantId, foodItemId);
            return ResponseEntity.ok(new DefaultResult("Food item deleted and unlinked from restaurant", false, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }


    @PutMapping("/{id}/ban")
    public ResponseEntity<String> banRestaurant(@PathVariable String id) {
        try {
            boolean success = service.banRestaurant(id);
            if (success) {
                return ResponseEntity.ok("Restaurant has been banned.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurant not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/unban")
    public ResponseEntity<String> unbanRestaurant(@PathVariable String id) {
        try {
            boolean success = service.unbanRestaurant(id);
            if (success) {
                return ResponseEntity.ok("Restaurant has been unbanned.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurant not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }


}
