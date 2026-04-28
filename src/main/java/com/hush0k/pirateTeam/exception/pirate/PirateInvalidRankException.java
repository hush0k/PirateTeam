package com.hush0k.pirateTeam.exception.pirate;

import java.util.UUID;

public class PirateInvalidRankException extends RuntimeException {
    public PirateInvalidRankException(UUID id) {
        super("Пират с такой ID: " + id + " имеет недопустимый ранг");
    }
}
