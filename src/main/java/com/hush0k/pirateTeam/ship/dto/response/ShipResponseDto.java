package com.hush0k.pirateTeam.ship.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hush0k.pirateTeam.pirate.enums.Country;
import com.hush0k.pirateTeam.ship.enums.ShipType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ShipResponseDto(
        UUID id,
        UUID capitanId,
        String name,
        LocalDate constructionDate,
        ShipType shipType,
        int maxCrew,
        float maxSpeed,
        Country builderCountry,
        int cargoCapacity,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Asia/Almaty")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Asia/Almaty")
        LocalDateTime updatedAt
) {}
