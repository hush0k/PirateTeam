package com.hush0k.pirateTeam.team.dto.request;

import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

public record TeamUpdateDto(
        @Size(min = 3, max = 100, message = "Название должно быть от 3 до 100 символов")
        String name,

        UUID capitanId,

        Set<UUID> pirateIds
) {}
