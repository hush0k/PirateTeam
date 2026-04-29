package com.hush0k.pirateTeam.team.dto.request;

import jakarta.validation.constraints.Positive;

public record TeamPowerChangeDto(
        @Positive(message = "Мощь должна быть положительным числом")
        int amount
) {
}
