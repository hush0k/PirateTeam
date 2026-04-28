package com.hush0k.pirateTeam.exception.team;

import java.util.Set;
import java.util.UUID;

public class PirateAlreadyInTeamException extends RuntimeException {
    public PirateAlreadyInTeamException(UUID teamId, Set<UUID> pirateIds) {
        super("Pirates with ids " + pirateIds + " are already in team with id " + teamId);
    }
}
