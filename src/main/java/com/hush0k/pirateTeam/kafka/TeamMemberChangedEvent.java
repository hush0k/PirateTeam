package com.hush0k.pirateTeam.kafka;

import java.util.Set;
import java.util.UUID;

public record TeamMemberChangedEvent(
        UUID teamId,
        Set<UUID> pirateIds,
        String action
) {}
