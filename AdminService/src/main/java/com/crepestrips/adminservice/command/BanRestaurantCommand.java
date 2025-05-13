package com.crepestrips.adminservice.command;


import com.crepestrips.adminservice.client.RestaurantServiceClient;
import com.crepestrips.adminservice.client.dto.RestaurantDTO;

public class BanRestaurantCommand implements AdminCommand {
    private final RestaurantServiceClient client;
    private final Long restaurantId;
    private boolean previousStatus;

    public BanRestaurantCommand(RestaurantServiceClient client, Long restaurantId) {
        this.client = client;
        this.restaurantId = restaurantId;
    }

    @Override
    public void execute() {
        try {
            RestaurantDTO restaurant = client.getRestaurant(restaurantId);
            this.previousStatus = !restaurant.isBanned();
            client.banRestaurant(restaurantId);
        } catch (Exception e) {
//            throw new CommandExecutionException("Failed to ban restaurant: " + restaurantId, e);
        }
    }

}
