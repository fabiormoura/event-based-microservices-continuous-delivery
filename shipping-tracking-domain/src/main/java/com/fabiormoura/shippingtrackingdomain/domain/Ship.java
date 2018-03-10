package com.fabiormoura.shippingtrackingdomain.domain;

import com.fabiormoura.shippingtrackingdomain.command.CreateShipCommand;
import com.fabiormoura.shippingtrackingdomain.event.ShipCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
public class Ship {
    @AggregateIdentifier
    private String id;

    @SuppressWarnings("unused")
    public Ship() {

    }

    @CommandHandler
    public Ship(CreateShipCommand createShipCommand) {
        apply(new ShipCreatedEvent(createShipCommand.getId()));
    }

    @EventSourcingHandler
    public void on(ShipCreatedEvent event) {
        this.id = event.getId();
    }
}
