package com.hush0k.pirateTeam.fleet.client.dto;

import com.hush0k.pirateTeam.pirate.enums.Country;
import com.hush0k.pirateTeam.ship.enums.ShipType;

import java.time.LocalDate;
import java.util.UUID;

public record ShipDto(
        UUID id,
        UUID capitanId,
        String name,
        ShipType shipType,
        int maxCrew,
        float maxSpeed,
        Country builderCountry,
        int cargoCapacity
) {}
