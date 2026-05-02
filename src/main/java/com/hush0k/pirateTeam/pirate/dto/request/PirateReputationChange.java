package com.hush0k.pirateTeam.pirate.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Positive;

public record PirateReputationChange(
        @JsonAlias("amount")
        @Positive(message = "Репутация должна быть положительным числом")
        int reputation
) {
}
