package com.hush0k.pirateTeam.exception.team;

import java.util.UUID;

public class InsufficientTreasuryException extends RuntimeException {
    public InsufficientTreasuryException(UUID id) {
        super("У команды с ID: " + id + " не достаточно золота");

    }
}
