package com.hush0k.pirateTeam.team.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record TeamMembersChangeDto(
        @NotNull(message = "Список участников не может быть null")
        @NotEmpty(message = "Список участников не может быть пустым")
        Set<UUID> pirates
) {}
