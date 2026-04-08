package com.hush0k.pirateTeam.auth.controller;

import com.hush0k.pirateTeam.auth.dto.request.LoginRequestDto;
import com.hush0k.pirateTeam.auth.dto.response.TokenResponseDto;
import com.hush0k.pirateTeam.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody @Valid LoginRequestDto dto) {
        return authService.login(dto);
    }

    @PostMapping("/refresh")
    public TokenResponseDto refresh(@RequestHeader("Refresh-Token") String refreshToken) {
        return authService.refresh(refreshToken);
    }
}