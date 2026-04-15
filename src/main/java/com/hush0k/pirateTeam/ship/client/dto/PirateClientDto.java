package com.hush0k.pirateTeam.ship.client.dto;

import com.hush0k.pirateTeam.pirate.enums.Rank;

import java.util.UUID;

public record PirateClientDto(
        UUID id,
        String firstName,
        String lastName,
        Rank rank
) {}
