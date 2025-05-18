package com.crepestrips.userservice.event; // Example
import com.crepestrips.userservice.dto.CartDto;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderPlacementRequestedEvent {
    private UUID eventId;
    private Instant eventTimestamp;
    private CartDto cartDetails;

    public OrderPlacementRequestedEvent(CartDto cartDetails) {
        this.eventId = UUID.randomUUID();
        this.eventTimestamp = Instant.now();
        this.cartDetails = cartDetails;
    }
}