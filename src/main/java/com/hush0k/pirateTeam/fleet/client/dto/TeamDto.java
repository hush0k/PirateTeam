package com.hush0k.pirateTeam.fleet.client.dto;

import java.util.Set;
import java.util.UUID;

public record TeamDto(
        UUID id,
        String name,
        UUID capitanId,
        int treasury,
        int reputation,
        int cohesion,
        int morale,
        int loyalty,
        int lootBonus,
        int power,
        int fatigue
) {}
