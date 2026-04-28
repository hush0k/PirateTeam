package com.hush0k.pirateTeam.exception.pirate;

import java.util.UUID;

public class PirateNotCaptainException extends RuntimeException {
    public PirateNotCaptainException(UUID id) {
        super("Pirate not captain with id: " + id);
    }
}
