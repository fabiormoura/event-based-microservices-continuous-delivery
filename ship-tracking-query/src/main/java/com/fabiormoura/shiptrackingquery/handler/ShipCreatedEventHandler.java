package com.fabiormoura.shiptrackingquery.handler;

import com.fabiormoura.shiptrackingquery.event.ShipCreatedEvent;
import com.fabiormoura.shiptrackingquery.repository.ShipViewRepository;
import com.fabiormoura.shiptrackingquery.view.ShipView;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShipCreatedEventHandler {
    private static Logger logger = LoggerFactory.getLogger(ShipCreatedEventHandler.class);
    private final ShipViewRepository repository;

    @Autowired
    public ShipCreatedEventHandler(ShipViewRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void handle(ShipCreatedEvent event) {
        repository.save(new ShipView(event.getId()));
        logger.info("Ship view created for {}", event.getId());
    }
}
