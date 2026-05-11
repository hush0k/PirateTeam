package com.hush0k.pirateTeam.team.dto.response;

import com.hush0k.pirateTeam.ship.dto.response.ShipResponseDto;

import java.util.UUID;

public record ShipSearchResultResponse(
        UUID teamId,
        boolean success,
        int chancePercent,
        int rollPercent,
        int abandonedShipsCount,
        int cohesionChange,
        int moraleChange,
        int reputationChange,
        ShipResponseDto ship
) {
}
