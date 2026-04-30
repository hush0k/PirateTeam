package com.hush0k.pirateTeam.team.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

public record TeamMoraleChangeDto(
        @Max(value = 100, message = "Мораль не может быть больше 100")
        @Positive(message = "Мораль должна быть положительным числом")
        int amount
) {
}
