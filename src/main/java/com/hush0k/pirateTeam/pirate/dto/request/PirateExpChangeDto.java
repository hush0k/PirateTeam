package com.hush0k.pirateTeam.pirate.dto.request;

import jakarta.validation.constraints.Positive;

public record PirateExpChangeDto(
        @Positive(message = "Опыт должен быть положительным числом")
        int amount
) {
}
