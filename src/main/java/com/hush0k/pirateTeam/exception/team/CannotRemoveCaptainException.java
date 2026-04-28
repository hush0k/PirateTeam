package com.hush0k.pirateTeam.exception.team;

import java.util.UUID;

public class CannotRemoveCaptainException extends RuntimeException {
    public CannotRemoveCaptainException(UUID teamId, UUID captainId) {
        super("Вы не можете удалить капитана " + captainId + " с команды " + teamId);
    }
}
