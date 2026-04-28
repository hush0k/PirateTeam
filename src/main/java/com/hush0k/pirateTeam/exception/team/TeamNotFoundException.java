package com.hush0k.pirateTeam.exception.team;

import java.util.UUID;

public class TeamNotFoundException extends RuntimeException {
    public TeamNotFoundException(UUID id) {
        super("Команда с такой ID: " + id + " не существует");
    }
}