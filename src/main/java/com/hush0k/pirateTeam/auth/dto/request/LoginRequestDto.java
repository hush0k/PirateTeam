package com.hush0k.pirateTeam.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank String login,
        @NotBlank String password
) {}