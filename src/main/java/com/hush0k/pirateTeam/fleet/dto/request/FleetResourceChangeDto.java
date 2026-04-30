package com.hush0k.pirateTeam.fleet.dto.request;

import jakarta.validation.constraints.Positive;

public record FleetResourceChangeDto(
        @Positive(message = "Amount must be positive")
        int amount
) {}
