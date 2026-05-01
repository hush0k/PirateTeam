package com.hush0k.pirateTeam.island.client.dto;

import java.util.UUID;

public record PirateClientDto(
        UUID id,
        String firstName,
        String lastName,
        int reputation,
        int treasury
) {
}
