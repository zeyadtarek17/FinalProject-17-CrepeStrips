package com.crepestrips.adminservice.command;


import com.crepestrips.adminservice.client.FoodItemServiceClient;
import com.crepestrips.adminservice.client.dto.FoodItemDTO;

public class SuspendFoodItemCommand implements AdminCommand {
    private final FoodItemServiceClient client;
    private final Long foodItemId;
    private boolean previousStatus;

    public SuspendFoodItemCommand(FoodItemServiceClient client, Long foodItemId) {
        this.client = client;
        this.foodItemId = foodItemId;
    }

    @Override
    public void execute()  {
        try {
            FoodItemDTO item = client.getFoodItem(foodItemId);
            this.previousStatus = item.isActive();
            client.suspendFoodItem(foodItemId);
        } catch (Exception e) {

        }
    }


}