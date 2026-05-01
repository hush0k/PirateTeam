package com.hush0k.pirateTeam.exception.island;

import java.util.UUID;

public class IslandNotOwnerException extends RuntimeException {
    public IslandNotOwnerException(UUID islandId,  UUID ownerId) {
        super("Пират по ID: " + ownerId + " не является владельцем острова с ID: " + islandId);
    }
}
