package com.hush0k.pirateTeam.exception.ship;

import java.util.UUID;

public class ShipNotFoundException extends RuntimeException {
    public ShipNotFoundException(UUID id) {
        super("Корабль с такой ID: " + id + " не существует");
    }
}
