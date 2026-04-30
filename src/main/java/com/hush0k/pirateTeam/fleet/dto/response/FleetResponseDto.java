package com.hush0k.pirateTeam.fleet.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record FleetResponseDto(
        UUID id,
        UUID ownerId,
        String name,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Asia/Almaty")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Asia/Almaty")
        LocalDateTime updatedAt
) {}
