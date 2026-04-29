package com.hush0k.pirateTeam.pirate.dto.request;

import jakarta.validation.constraints.Positive;

public record PirateIntelligenceChangeDto(
        @Positive(message = "Интеллект должен быть положительным числом")
        int amount
) {
}
