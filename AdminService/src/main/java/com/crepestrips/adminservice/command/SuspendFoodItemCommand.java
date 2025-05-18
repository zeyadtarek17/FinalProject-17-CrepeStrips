package com.crepestrips.adminservice.command;

import com.crepestrips.adminservice.client.FoodItemServiceClient;
import com.crepestrips.adminservice.client.dto.FoodItemDTO;
// Make sure to import FeignException if you catch it specifically
import feign.FeignException; // Add this import

public class SuspendFoodItemCommand implements AdminCommand {

    private final FoodItemServiceClient client;
    private final String foodItemId;

    public SuspendFoodItemCommand(FoodItemServiceClient client, String foodItemId) {
        this.client = client;
        this.foodItemId = foodItemId;
    }

    @Override
    public void execute() {
        System.out.println("DEBUG: Entered SuspendFoodItemCommand.execute() for foodItemId: " + foodItemId);
        try {
            client.suspendFoodItem(foodItemId);
        } catch (FeignException fe) {
            System.err.println(
                    "ERROR: FeignException caught in SuspendFoodItemCommand.execute() for foodItemId: " + foodItemId);
            System.err.println("FeignException status: " + fe.status());
            System.err.println("FeignException message: " + fe.getMessage());
            if (fe.responseBody().isPresent()) {
                System.err.println("FeignException response body: " + fe.contentUTF8());
            }
            fe.printStackTrace();
        } catch (Exception e) {
            System.err.println("ERROR: Generic Exception caught in SuspendFoodItemCommand.execute() for foodItemId: "
                    + foodItemId);
            e.printStackTrace();
        }
        System.out.println("DEBUG: Exiting SuspendFoodItemCommand.execute() for foodItemId: " + foodItemId);
    }
}