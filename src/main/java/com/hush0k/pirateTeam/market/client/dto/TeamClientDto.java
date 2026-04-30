package com.hush0k.pirateTeam.market.client.dto;

import java.util.UUID;

public record TeamClientDto(
        UUID id,
        String name,
        UUID capitanId,
        int treasury
) {}
