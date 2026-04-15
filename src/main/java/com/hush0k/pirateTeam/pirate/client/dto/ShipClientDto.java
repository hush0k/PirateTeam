package com.hush0k.pirateTeam.pirate.client.dto;

import com.hush0k.pirateTeam.ship.enums.ShipType;

import java.util.UUID;

public record ShipClientDto(
        UUID id,
        String name,
        ShipType shipType
) {}
