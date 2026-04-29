package com.hush0k.pirateTeam.pirate.dto.request;

import jakarta.validation.constraints.Positive;

public record PirateTreasuryChangeDto(
        @Positive(message = "Сумма должна быть положительным числом")
        int amount
) {
}
