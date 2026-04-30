package com.hush0k.pirateTeam.team.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;

public record TeamReputationChangeDto(
        @Max(value = 100, message = "Репутация не может быть больше 100")
        @PositiveOrZero(message = "Репутация не может быть отрицательной")
        int reputation
) {}
