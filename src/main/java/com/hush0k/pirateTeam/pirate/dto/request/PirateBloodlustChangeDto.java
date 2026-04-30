package com.hush0k.pirateTeam.pirate.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

public record PirateBloodlustChangeDto(
        @Max(value = 100, message = "Жажда крови не может быть больше 100")
        @Positive(message = "Жажда крови должна быть положительным числом")
        int amount
) {
}
