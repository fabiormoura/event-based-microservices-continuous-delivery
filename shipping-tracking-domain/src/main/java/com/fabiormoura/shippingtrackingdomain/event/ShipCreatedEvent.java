package com.fabiormoura.shippingtrackingdomain.event;

public class ShipCreatedEvent {
    private final String id;

    public ShipCreatedEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
