package com.fabiormoura.shiptrackingdomain.event;

import java.io.Serializable;

public class ShipCreatedEvent implements Serializable {
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
