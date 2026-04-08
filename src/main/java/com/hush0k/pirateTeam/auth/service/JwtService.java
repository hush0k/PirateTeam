package com.hush0k.pirateTeam.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.accessTokenExpirationTime}") long accessTokenExpirationTime,
            @Value("${jwt.refreshTokenExpirationTime}") long refreshTokenExpirationTime
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    public String generateAccessToken(UUID id, String login) {
        return buildToken(id, login, accessTokenExpirationTime);
    }

    public String generateRefreshToken(UUID id, String login) {
        return buildToken(id, login, refreshTokenExpirationTime);
    }

    private String buildToken(UUID pirateId, String login, long expiration) {
        Date now = new Date();
        return Jwts.builder()
                .subject(login)
                .claim("pirateId", pirateId.toString())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiration))
                .signWith(secretKey)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractLogin(String token) {
        return extractAllClaims(token).getSubject();
    }

    public UUID extractPirateId(String token) {
        return UUID.fromString(extractAllClaims(token).get("pirateId", String.class));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
