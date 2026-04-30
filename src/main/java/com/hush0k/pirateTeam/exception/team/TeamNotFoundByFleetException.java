package com.hush0k.pirateTeam.exception.team;

import java.util.UUID;

public class TeamNotFoundByFleetException extends RuntimeException {
    public TeamNotFoundByFleetException(UUID fleetId) {
        super("Команда с таким ID флота: " + fleetId + " не существует");
    }
}
