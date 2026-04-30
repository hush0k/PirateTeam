package com.hush0k.pirateTeam.exception.fleet;

import java.util.UUID;

public class FleetNotFoundException extends RuntimeException {
    public FleetNotFoundException(UUID id) {
        super("Флот с такой ID: " + id + " не существует");
    }
}
