package com.hush0k.pirateTeam.fleet.dto.response;

import java.util.UUID;

public record FleetFindTreasure(
        UUID teamId,
        int treasury
) {
}
