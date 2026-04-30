package com.hush0k.pirateTeam.team.client.dto;

import com.hush0k.pirateTeam.pirate.enums.Freedom;
import com.hush0k.pirateTeam.pirate.enums.Rank;

import java.util.UUID;

public record CaptainClientDto(
        UUID id,
        String firstName,
        String lastName,
        Rank rank,
        int reputation,
        Freedom freedom
) {}
