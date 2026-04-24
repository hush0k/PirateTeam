package com.hush0k.pirateTeam.island.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hush0k.pirateTeam.island.enums.IslandLevel;
import com.hush0k.pirateTeam.island.enums.IslandLocation;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record IslandResponseDto(
        UUID id,
        String name,
        String nickname,
        double area,
        IslandLocation location,
        UUID ownerId,
        int population,
        int shipTrafficPerDay,
        long goldTurnover,
        double taxPercentage,
        IslandLevel level,
        Set<UUID> legendaryPirateIds,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Asia/Almaty")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Asia/Almaty")
        LocalDateTime updatedAt
) {}
