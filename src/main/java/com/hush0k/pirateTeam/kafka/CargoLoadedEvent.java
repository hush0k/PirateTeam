package com.hush0k.pirateTeam.kafka;

import java.util.UUID;

public record CargoLoadedEvent(
        UUID fleetId,
        int amount
) {
}
