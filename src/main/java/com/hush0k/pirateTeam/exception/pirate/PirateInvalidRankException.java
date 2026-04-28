package com.hush0k.pirateTeam.exception.pirate;

import java.util.UUID;

public class PirateInvalidRankException extends RuntimeException {
    public PirateInvalidRankException(UUID id) {
        super("Pirate with id " + id + " has unprohibited rank");
    }
}
