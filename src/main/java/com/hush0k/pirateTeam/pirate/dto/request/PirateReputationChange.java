package com.hush0k.pirateTeam.pirate.dto.request;

import jakarta.validation.constraints.Positive;

public record PirateReputationChange(
        @Positive(message = "Репутация должна быть положительным числом")
        int reputation
) {
}
