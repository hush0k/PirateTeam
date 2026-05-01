package com.hush0k.pirateTeam.island.client.dto;

import java.util.UUID;

public record FleetClientDto(
        UUID id,
        int coordinateX,
        int coordinateY
) {}
