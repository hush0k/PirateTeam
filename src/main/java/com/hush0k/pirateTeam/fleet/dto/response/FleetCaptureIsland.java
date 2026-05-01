package com.hush0k.pirateTeam.fleet.dto.response;

import java.util.UUID;

public record FleetCaptureIsland(
        UUID fleetId,
        UUID islandId,
        int lostAmmo
) {
}
