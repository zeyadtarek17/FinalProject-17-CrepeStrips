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
            System.out.println("DEBUG: Attempting to call client.getFoodItem(" + foodItemId + ")");
            FoodItemDTO item = client.getFoodItem(foodItemId);

            if (item == null) {
                System.out.println("DEBUG: client.getFoodItem returned null for foodItemId: " + foodItemId);
                System.out.println("Food item " + foodItemId + " not found or client returned null.");
                return; // Exit if item is null
            }

            System.out.println("DEBUG: client.getFoodItem returned. Item ID: " + item.getId() + ", Active: " + item.isActive()); // Assuming FoodItemDTO has getId() and isActive()

            if (item.isActive()) {
                System.out.println("DEBUG: Item " + foodItemId + " is active. Attempting to call client.suspendFoodItem(" + foodItemId + ")");
                client.suspendFoodItem(foodItemId);
                System.out.println("Food item " + foodItemId + " suspended."); // Your target message
                System.out.println("DEBUG: Successfully called client.suspendFoodItem(" + foodItemId + ")");
            } else {
                System.out.println("Food item " + foodItemId + " is not active. Current active status: " + item.isActive() + ". Not suspending.");
            }
        } catch (FeignException fe) {
            System.err.println("ERROR: FeignException caught in SuspendFoodItemCommand.execute() for foodItemId: " + foodItemId);
            System.err.println("FeignException status: " + fe.status());
            System.err.println("FeignException message: " + fe.getMessage());
            if (fe.responseBody().isPresent()) {
                System.err.println("FeignException response body: " + fe.contentUTF8());
            }
            fe.printStackTrace();
        } catch (Exception e) {
            System.err.println("ERROR: Generic Exception caught in SuspendFoodItemCommand.execute() for foodItemId: " + foodItemId);
            e.printStackTrace();
        }
        System.out.println("DEBUG: Exiting SuspendFoodItemCommand.execute() for foodItemId: " + foodItemId);
    }
}