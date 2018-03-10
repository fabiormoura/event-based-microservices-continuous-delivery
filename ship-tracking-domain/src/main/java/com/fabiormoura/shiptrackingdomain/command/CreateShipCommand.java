package com.fabiormoura.shiptrackingdomain.command;

import org.axonframework.common.IdentifierFactory;

public class CreateShipCommand {
    private String id;

    public CreateShipCommand() {
        this.id = IdentifierFactory.getInstance().generateIdentifier();
    }

    public String getId() {
        return id;
    }
}
