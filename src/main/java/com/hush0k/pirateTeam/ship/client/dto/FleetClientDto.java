package com.hush0k.pirateTeam.ship.client.dto;

import java.util.UUID;

public record FleetClientDto(
        UUID id,
        UUID ownerId,
        String name
) {}
