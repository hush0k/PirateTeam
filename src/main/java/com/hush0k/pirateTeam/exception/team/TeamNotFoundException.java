package com.hush0k.pirateTeam.exception.team;

import java.util.UUID;

public class TeamNotFoundException extends RuntimeException {
    public TeamNotFoundException(UUID id) {
        super("Team with id " + id + " not found");
    }
}