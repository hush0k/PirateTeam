package com.hush0k.pirateTeam.market.client.dto;

import java.util.UUID;

public record FleetClientDto(
        UUID id,
        UUID ownerId,
        String name,
        int ammo,
        int provision
) {}
