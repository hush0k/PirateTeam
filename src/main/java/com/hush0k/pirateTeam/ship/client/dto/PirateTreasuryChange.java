package com.hush0k.pirateTeam.ship.client.dto;

import jakarta.validation.constraints.Positive;

public record PirateTreasuryChange(
        @Positive(message = "Сумма изменения должна быть положительной")
        int amount
) {
}
