package com.hush0k.pirateTeam.kafka;

import java.util.UUID;

public record CapitanAssignedEvent(
        UUID shipId,
        UUID pirateId
) {}
