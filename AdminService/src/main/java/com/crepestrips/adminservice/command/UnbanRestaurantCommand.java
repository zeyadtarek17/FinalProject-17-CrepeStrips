package com.crepestrips.adminservice.command;


import com.crepestrips.adminservice.client.RestaurantServiceClient;
import com.crepestrips.adminservice.client.dto.FoodItemDTO;
import com.crepestrips.adminservice.client.dto.RestaurantDTO;
import feign.FeignException;

public class UnbanRestaurantCommand implements AdminCommand {
    private final RestaurantServiceClient client;
    private final String restaurantId;


    public UnbanRestaurantCommand(RestaurantServiceClient client, String restaurantId) {
        this.client = client;
        this.restaurantId = restaurantId;
    }

    @Override
    public void execute() {
        System.out.println("DEBUG: Entered unbanRestaurantCommand.execute() for restaurant: " + restaurantId);
        try {
            System.out.println("DEBUG: Attempting to call client.getRestaurant(" + restaurantId + ")");
            RestaurantDTO restaurant = client.getRestaurant(restaurantId);

            if (restaurant == null) {
                System.out.println("DEBUG: client.getRestaurant returned null for restaurantID: " + restaurantId);
                System.out.println("Restaurant " + restaurantId + " not found or client returned null.");
                return; // Exit if item is null
            }

            System.out.println("DEBUG: client.getRestaurant returned. Restaurant ID: " + restaurant.getId() ); // Assuming FoodItemDTO has getId() and isActive()

           client.activateRestaurant(restaurantId);
        } catch (FeignException fe) {
            System.err.println("ERROR: FeignException caught in SuspendFoodItemCommand.execute() for foodItemId: " + restaurantId);
            System.err.println("FeignException status: " + fe.status());
            System.err.println("FeignException message: " + fe.getMessage());
            if (fe.responseBody().isPresent()) {
                System.err.println("FeignException response body: " + fe.contentUTF8());
            }
            fe.printStackTrace();
        } catch (Exception e) {
            System.err.println("ERROR: Generic Exception caught in SuspendFoodItemCommand.execute() for foodItemId: " + restaurantId);
            e.printStackTrace();
        }
        System.out.println("DEBUG: Exiting SuspendFoodItemCommand.execute() for foodItemId: " + restaurantId);
    }

}
