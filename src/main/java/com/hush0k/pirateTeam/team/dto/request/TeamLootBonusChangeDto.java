package com.hush0k.pirateTeam.team.dto.request;

import jakarta.validation.constraints.Positive;

public record TeamLootBonusChangeDto(
        @Positive(message = "Бонус к добыче должен быть положительным числом")
        int amount
) {
}
