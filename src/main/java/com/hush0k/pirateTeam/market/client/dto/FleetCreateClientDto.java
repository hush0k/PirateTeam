package com.hush0k.pirateTeam.market.client.dto;

import java.util.UUID;

public record FleetCreateClientDto(
        UUID ownerId,
        String name
) {}