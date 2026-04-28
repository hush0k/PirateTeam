package com.hush0k.pirateTeam.exception.team;

import java.util.Set;
import java.util.UUID;

public class PirateNotInTeamException extends RuntimeException {
    public PirateNotInTeamException(UUID teamId, Set<UUID> pirateIds) {
        super("Пираты с этой ID: " + pirateIds + " нет в этой команде " + teamId);
    }
}
