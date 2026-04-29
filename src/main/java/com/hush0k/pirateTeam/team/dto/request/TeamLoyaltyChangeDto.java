package com.hush0k.pirateTeam.team.dto.request;

import jakarta.validation.constraints.Positive;

public record TeamLoyaltyChangeDto(
        @Positive(message = "Лояльность должна быть положительным числом")
        int amount
) {
}
