package com.hush0k.pirateTeam.team.dto.request;

import jakarta.validation.constraints.*;

import java.util.Set;
import java.util.UUID;

public record TeamCreateDto(

        @NotBlank(message = "Название команды не может быть пустым")
        @Size(min = 3, max = 100, message = "Название должно быть от 3 до 100 символов")
        String name,

        UUID capitanId,

        Set<UUID> pirateIds,

        @PositiveOrZero(message = "Казна не может быть отрицательной")
        int treasury,

        @PositiveOrZero(message = "Репутация не может быть отрицательной")
        int reputation,

        @PositiveOrZero(message = "Сплоченность не может быть отрицательной")
        int cohesion

) {}