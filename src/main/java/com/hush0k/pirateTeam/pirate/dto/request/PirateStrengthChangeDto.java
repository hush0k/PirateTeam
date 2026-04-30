package com.hush0k.pirateTeam.pirate.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

public record PirateStrengthChangeDto(
        @Max(value = 100, message = "Сила не может быть больше 100")
        @Positive(message = "Сила должна быть положительным числом")
        int amount
) {
}
