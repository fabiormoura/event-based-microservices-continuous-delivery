package com.fabiormoura.shippingtrackingdomain.api;

import com.fabiormoura.shippingtrackingdomain.command.CreateShipCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.ACCEPTED;

@RestController
@RequestMapping("/ships")
public class ShipsController {
    private final CommandGateway gateway;

    public ShipsController(CommandGateway gateway) {
        this.gateway = gateway;
    }

    @ResponseStatus(ACCEPTED)
    @PostMapping
    public CreateShipCommand create() {
        CreateShipCommand command = new CreateShipCommand();
        gateway.send(command);
        return command;
    }
}
