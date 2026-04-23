package com.hush0k.pirateTeam.team.dto.request;

import jakarta.validation.constraints.PositiveOrZero;

public record TeamTreasuryChangeDto(
        @PositiveOrZero(message = "Казна не может быть отрицательной")
        int treasury
) {}
