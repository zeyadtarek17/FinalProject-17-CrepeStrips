//package com.crepestrips.adminservice.command;
//
//public class BanRestaurantCommand implements AdminCommand {
//    private final ModerationService moderationService;
//    private final String restaurantId;
//
//    public BanRestaurantCommand(ModerationService moderationService, String restaurantId) {
//        this.moderationService = moderationService;
//        this.restaurantId = restaurantId;
//    }
//
//    @Override
//    public void execute() {
//        moderationService.banRestaurant(restaurantId);
//    }
//}