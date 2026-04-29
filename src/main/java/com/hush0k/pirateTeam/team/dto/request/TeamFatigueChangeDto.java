package com.hush0k.pirateTeam.team.dto.request;

import jakarta.validation.constraints.Positive;

public record TeamFatigueChangeDto(
        @Positive(message = "Усталость должна быть положительным числом")
        int amount
) {
}
