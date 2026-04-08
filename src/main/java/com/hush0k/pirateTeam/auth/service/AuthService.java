package com.hush0k.pirateTeam.auth.service;

import com.hush0k.pirateTeam.auth.dto.request.LoginRequestDto;
import com.hush0k.pirateTeam.auth.dto.response.TokenResponseDto;
import com.hush0k.pirateTeam.pirate.domain.Pirate;
import com.hush0k.pirateTeam.pirate.repository.PirateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PirateRepository pirateRepository;

    public TokenResponseDto login(LoginRequestDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.login(), dto.password())
        );

        Pirate pirate = pirateRepository.findByLogin(dto.login())
                .orElseThrow(() -> new UsernameNotFoundException("Pirate not found: " + dto.login()));

        return new TokenResponseDto(
                jwtService.generateAccessToken(pirate.getId(), pirate.getLogin()),
                jwtService.generateRefreshToken(pirate.getId(), pirate.getLogin())
        );
    }

    public TokenResponseDto refresh(String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String login = jwtService.extractLogin(refreshToken);

        Pirate pirate = pirateRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Pirate not found: " + login));

        return new TokenResponseDto(
                jwtService.generateAccessToken(pirate.getId(), pirate.getLogin()),
                jwtService.generateRefreshToken(pirate.getId(), pirate.getLogin())
        );
    }
}