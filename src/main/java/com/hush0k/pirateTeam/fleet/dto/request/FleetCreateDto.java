package com.hush0k.pirateTeam.fleet.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record FleetCreateDto(
        @NotNull(message = "ID владельца не может быть null")
        UUID ownerId,

        @Size(min = 3, max = 100, message = "Название должно быть от 3 до 100 символов")
        String name
) {}
