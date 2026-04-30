package com.hush0k.pirateTeam.market.client.dto;

public record FleetClientStatsDto(
        int totalPower,
        double avgSpeed,
        int maxCrew,
        int maxCargo,
        int filledCargoSpace
) {}
