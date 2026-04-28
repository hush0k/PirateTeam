package com.hush0k.pirateTeam.exception.team;

import java.util.Set;
import java.util.UUID;

public class PirateNotInTeamException extends RuntimeException {
    public PirateNotInTeamException(UUID teamId, Set<UUID> pirateIds) {
        super("Pirates with ids " + pirateIds + " are not in team with id " + teamId);
    }
}
