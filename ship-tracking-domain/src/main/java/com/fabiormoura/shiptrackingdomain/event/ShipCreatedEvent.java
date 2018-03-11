package com.fabiormoura.shiptrackingdomain.event;

public class ShipCreatedEvent {
    private String id;

    @SuppressWarnings("unchecked")
    public ShipCreatedEvent() {
    }

    public ShipCreatedEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
