package com.hush0k.pirateTeam.fleet.client.dto;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public record TeamDto(
        UUID id,
        String name,
        UUID capitanId,
        Set<UUID> pirateIds,
        int treasury,
        int reputation,
        int cohesion,
        int morale,
        int loyalty,
        int lootBonus,
        int power,
        int fatigue
) {}
