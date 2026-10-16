package com.example.demo.security;


import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // Secret key must be long enough for HS256
    private final String SECRET_KEY =
            "BankingApplicationSecretKeyForJWTAuthentication2026KLUniversity";

    private final SecretKey key =
            Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Generate JWT token
    public String generateToken(String username) {

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                + 1000 * 60 * 30
                        )
                )
                .signWith(key)
                .compact();
    }

    // Extract username from token
    public String extractUsername(String token) {

        return extractAllClaims(token).getSubject();
    }

    // Validate token
    public boolean isTokenValid(String token) {

        try {

            Claims claims = extractAllClaims(token);

            return claims.getExpiration()
                    .after(new Date());

        } catch (Exception e) {

            return false;
        }
    }

    // Extract all claims
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}