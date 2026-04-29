package com.hush0k.pirateTeam.exception.pirate;

import java.util.UUID;

public class InsufficientPirateTreasuryException extends RuntimeException {
    public InsufficientPirateTreasuryException(UUID id) {
        super("У пирата с ID: " + id + " недостаточно золота");
    }
}
