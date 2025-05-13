package com.crepestrips.orderservice.dto;

import java.util.UUID;

public class UserMessage {
    private UUID userId;

    public UserMessage() {}

    public UserMessage(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
