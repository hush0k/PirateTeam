package com.hush0k.pirateTeam.team.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

public record TeamLoyaltyChangeDto(
        @Max(value = 100, message = "Лояльность не может быть больше 100")
        @Positive(message = "Лояльность должна быть положительным числом")
        int amount
) {
}
