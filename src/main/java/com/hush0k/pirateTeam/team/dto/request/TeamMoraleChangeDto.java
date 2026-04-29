package com.hush0k.pirateTeam.team.dto.request;

import jakarta.validation.constraints.Positive;

public record TeamMoraleChangeDto(
        @Positive(message = "Мораль должна быть положительным числом")
        int amount
) {
}
