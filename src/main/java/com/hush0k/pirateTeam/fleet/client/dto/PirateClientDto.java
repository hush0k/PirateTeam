package com.hush0k.pirateTeam.fleet.client.dto;

import java.util.UUID;

public record PirateClientDto(
        UUID teamId,
        int bloodlust,
        int intelligence,
        int strength
) {
}
