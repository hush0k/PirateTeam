package com.hush0k.pirateTeam.team.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

public record TeamFatigueChangeDto(
        @Max(value = 100, message = "Усталость не может быть больше 100")
        @Positive(message = "Усталость должна быть положительным числом")
        int amount
) {
}
