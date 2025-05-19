package com.crepestrips.adminservice.command;

import com.crepestrips.adminservice.client.RestaurantServiceClient;
import com.crepestrips.adminservice.client.dto.FoodItemDTO;
import com.crepestrips.adminservice.client.dto.RestaurantDTO;
import feign.FeignException;

public class BanRestaurantCommand implements AdminCommand {
    private final RestaurantServiceClient client;
    private final String restaurantId;

    public BanRestaurantCommand(RestaurantServiceClient client, String restaurantId) {
        this.client = client;
        this.restaurantId = restaurantId;
    }

    @Override
    public void execute() {
        System.out.println("DEBUG: Entered BanRestaurantCommand.execute() for restaurant: " + restaurantId);
        try {
            client.banRestaurant(restaurantId);
        } catch (FeignException fe) {
            System.err.println(
                    "ERROR: FeignException caught in SuspendFoodItemCommand.execute() for foodItemId: " + restaurantId);
            System.err.println("FeignException status: " + fe.status());
            System.err.println("FeignException message: " + fe.getMessage());
            if (fe.responseBody().isPresent()) {
                System.err.println("FeignException response body: " + fe.contentUTF8());
            }
            fe.printStackTrace();
        } catch (Exception e) {
            System.err.println("ERROR: Generic Exception caught in SuspendFoodItemCommand.execute() for foodItemId: "
                    + restaurantId);
            e.printStackTrace();
        }
        System.out.println("DEBUG: Exiting SuspendFoodItemCommand.execute() for foodItemId: " + restaurantId);
    }

}
