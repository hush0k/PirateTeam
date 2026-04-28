package com.hush0k.pirateTeam.exception.island;

import java.util.UUID;

public class IslandNotFoundException extends RuntimeException {
    public IslandNotFoundException(UUID id) {
        super("Island with id " + id + " not found");
    }
}
