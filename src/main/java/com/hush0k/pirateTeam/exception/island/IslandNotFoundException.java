package com.hush0k.pirateTeam.exception.island;

import java.util.UUID;

public class IslandNotFoundException extends RuntimeException {
    public IslandNotFoundException(UUID id) {
        super("Остров с такой ID: " + id + " не существует");
    }
}
