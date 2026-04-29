package com.hush0k.pirateTeam.pirate.dto.request;

import jakarta.validation.constraints.Positive;

public record PirateStrengthChangeDto(
        @Positive(message = "Сила должна быть положительным числом")
        int amount
) {
}
