package com.hush0k.pirateTeam.fleet.dto.request;

import jakarta.validation.constraints.Size;

public record FleetUpdateDto(
        @Size(min = 3, max = 100, message = "Название должно быть от 3 до 100 символов")
        String name,

        int coordinateX,
        int coordinateY
) {}
