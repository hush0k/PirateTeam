package com.hush0k.pirateTeam.team.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

public record TeamLootBonusChangeDto(
        @Max(value = 100, message = "Бонус к добыче не может быть больше 100")
        @Positive(message = "Бонус к добыче должен быть положительным числом")
        int amount
) {
}
