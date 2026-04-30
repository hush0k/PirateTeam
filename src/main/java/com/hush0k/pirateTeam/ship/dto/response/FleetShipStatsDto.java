package com.hush0k.pirateTeam.ship.dto.response;

public record FleetShipStatsDto(

        int totalPower,
        double avgSpeed,
        int maxCrew,
        int maxCargo,
        int filledCargoSpace

) {}
