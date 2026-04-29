package com.hush0k.pirateTeam.pirate.dto.request;

import jakarta.validation.constraints.Positive;

public record PirateBloodlustChangeDto(
        @Positive(message = "Жажда крови должна быть положительным числом")
        int amount
) {
}
