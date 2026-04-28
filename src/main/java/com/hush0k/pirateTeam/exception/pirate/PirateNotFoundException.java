package com.hush0k.pirateTeam.exception.pirate;

import java.util.UUID;

public class PirateNotFoundException extends RuntimeException {
    public PirateNotFoundException(UUID id) {
        super("Пират с такой ID: " + id + " не существует");
    }
}
