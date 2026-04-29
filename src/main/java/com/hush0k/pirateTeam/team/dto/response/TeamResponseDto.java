package com.hush0k.pirateTeam.team.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record TeamResponseDto(
        UUID id,
        String name,
        UUID capitanId,
        UUID fleetId,
        Set<UUID> pirateIds,
        int treasury,
        int reputation,
        int cohesion,
        int morale,
        int loyalty,
        int lootBonus,
        int power,
        int fatigue,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Asia/Almaty")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Asia/Almaty")
        LocalDateTime updatedAt
) {}