package com.hush0k.pirateTeam.pirate.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

public record PirateIntelligenceChangeDto(
        @Max(value = 100, message = "Интеллект не может быть больше 100")
        @Positive(message = "Интеллект должен быть положительным числом")
        int amount
) {
}
