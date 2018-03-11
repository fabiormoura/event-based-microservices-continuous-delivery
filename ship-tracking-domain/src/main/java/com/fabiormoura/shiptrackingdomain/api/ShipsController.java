package com.fabiormoura.shiptrackingdomain.api;

import com.fabiormoura.shiptrackingdomain.command.CreateShipCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

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
    public CompletableFuture<Object> create() {
        CreateShipCommand command = new CreateShipCommand();
        return gateway.send(command);
    }
}
