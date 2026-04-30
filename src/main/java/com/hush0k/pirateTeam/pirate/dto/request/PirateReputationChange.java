package com.hush0k.pirateTeam.pirate.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

public record PirateReputationChange(
        @Max(value = 100, message = "Репутация не может быть больше 100")
        @Positive(message = "Репутация должна быть положительным числом")
        int reputation
) {
}
