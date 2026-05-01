package com.hush0k.pirateTeam.fleet.client.dto;

import com.hush0k.pirateTeam.island.enums.DefenceType;
import com.hush0k.pirateTeam.island.enums.IslandLevel;

import java.util.Set;
import java.util.UUID;

public record IslandClientDto(
        UUID id,
        UUID ownerId,
        String name,
        int coordinateX,
        int coordinateY,
        DefenceType defenseType,
        IslandLevel level,
        Set<UUID> legendaryPirateIds,
        int population
) {
}
