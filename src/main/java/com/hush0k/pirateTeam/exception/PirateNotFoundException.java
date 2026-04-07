package com.hush0k.pirateTeam.exception;

import java.util.UUID;

public class PirateNotFoundException extends RuntimeException {
    public PirateNotFoundException(UUID id) {
        super("Pirate with id " + id + " not found");
    }
}
