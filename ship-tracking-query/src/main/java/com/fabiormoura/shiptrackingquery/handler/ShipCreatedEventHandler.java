package com.fabiormoura.shiptrackingquery.handler;

import com.fabiormoura.shiptrackingquery.event.ShipCreatedEvent;
import com.fabiormoura.shiptrackingquery.repository.ShipViewRepository;
import com.fabiormoura.shiptrackingquery.view.ShipView;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShipCreatedEventHandler {
    private final ShipViewRepository repository;

    @Autowired
    public ShipCreatedEventHandler(ShipViewRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void handle(ShipCreatedEvent event) {
        repository.save(new ShipView(event.getId()));
    }
}
