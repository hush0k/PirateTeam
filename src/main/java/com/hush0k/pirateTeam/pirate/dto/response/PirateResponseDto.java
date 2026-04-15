package com.hush0k.pirateTeam.pirate.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hush0k.pirateTeam.pirate.enums.Country;
import com.hush0k.pirateTeam.pirate.enums.Rank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record PirateResponseDto(

        UUID id,
        Set<UUID> shipIds,
        String login,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        Rank rank,
        int reputation,
        Country country,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Asia/Almaty")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Asia/Almaty")
        LocalDateTime updatedAt

) {}
