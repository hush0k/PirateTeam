package com.hush0k.pirateTeam.fleet.client.dto;

import java.util.UUID;

public record PirateClientDto(
        UUID pirateId,
        UUID teamId,
        int reputation,
        int bloodlust,
        int intelligence,
        int strength
) {
}
