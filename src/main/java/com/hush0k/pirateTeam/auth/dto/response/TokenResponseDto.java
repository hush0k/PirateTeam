package com.hush0k.pirateTeam.auth.dto.response;

public record TokenResponseDto(
        String accessToken,
        String refreshToken
) {}