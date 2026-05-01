package com.hush0k.pirateTeam.island.dto.request;

import jakarta.validation.constraints.Positive;

public record IslandTaxChangeDto(
        @Positive(message = "Значение налога должно быть положительным")
        double amount
) {
}
