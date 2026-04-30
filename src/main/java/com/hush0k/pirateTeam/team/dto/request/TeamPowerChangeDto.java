package com.hush0k.pirateTeam.team.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

public record TeamPowerChangeDto(
        @Max(value = 100, message = "Мощь не может быть больше 100")
        @Positive(message = "Мощь должна быть положительным числом")
        int amount
) {
}
