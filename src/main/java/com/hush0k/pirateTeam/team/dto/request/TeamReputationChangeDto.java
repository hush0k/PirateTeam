package com.hush0k.pirateTeam.team.dto.request;

import jakarta.validation.constraints.PositiveOrZero;

public record TeamReputationChangeDto(
        @PositiveOrZero(message = "Репутация не может быть отрицательной")
        int reputation
) {}
