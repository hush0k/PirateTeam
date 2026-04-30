package com.hush0k.pirateTeam.fleet.client.dto;

public record FleetShipStatsDto(

        int totalPower,
        double avgSpeed,
        int maxCrew,
        int maxCargo,
        int filledCargoSpace

) {}
