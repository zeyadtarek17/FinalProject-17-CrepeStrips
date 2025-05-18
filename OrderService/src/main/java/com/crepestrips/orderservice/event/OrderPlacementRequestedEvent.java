package com.crepestrips.orderservice.event; 

import com.crepestrips.orderservice.dto.CartDto;
import java.time.Instant;
import java.util.UUID;
// Consider Lombok

public class OrderPlacementRequestedEvent {

    private UUID eventId;         
    private Instant eventTimestamp; 
    private CartDto cartDetails;    


    // Constructors
    public OrderPlacementRequestedEvent() {
        this.eventId = UUID.randomUUID();
        this.eventTimestamp = Instant.now();
    }

    public OrderPlacementRequestedEvent(CartDto cartDetails) {
        this(); // Calls the no-arg constructor to set eventId and timestamp
        this.cartDetails = cartDetails;
    }

    // Getters and Setters
    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }
    public Instant getEventTimestamp() { return eventTimestamp; }
    public void setEventTimestamp(Instant eventTimestamp) { this.eventTimestamp = eventTimestamp; }
    public CartDto getCartDetails() { return cartDetails; }
    public void setCartDetails(CartDto cartDetails) { this.cartDetails = cartDetails; }

    @Override
    public String toString() {
        return "OrderPlacementRequestedEvent{" +
               "eventId=" + eventId +
               ", eventTimestamp=" + eventTimestamp +
               ", cartDetails=" + cartDetails +
               '}';
    }
}