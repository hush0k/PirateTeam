package com.hush0k.pirateTeam.kafka;

import java.util.UUID;

public record PirateStatChangedEvent(
        UUID pirateId,
        String statType,
        int amount
) {}
