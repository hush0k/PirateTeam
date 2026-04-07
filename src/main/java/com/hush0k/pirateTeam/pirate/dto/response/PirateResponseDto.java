package com.hush0k.pirateTeam.pirate.dto.response;

import com.hush0k.pirateTeam.pirate.enums.Country;
import com.hush0k.pirateTeam.pirate.enums.Rank;

import java.time.LocalDate;
import java.util.UUID;

public record PirateResponseDto(

        UUID id,
        String login,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        Rank rank,
        int reputation,
        Country country,
        LocalDate createdAt,
        LocalDate updateAt

) {}
