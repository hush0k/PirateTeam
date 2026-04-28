package com.hush0k.pirateTeam.team.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record TeamTreasuryChangeDto(
        @PositiveOrZero(message = "Казна не может быть отрицательной")
        int amount
) {}
