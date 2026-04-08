package com.hush0k.pirateTeam.exception;

import java.util.UUID;

public class ShipNotFoundException extends RuntimeException {
    public ShipNotFoundException(UUID id) {
        super("Ship with id " + id + " not found");
    }
}
