package com.example.demo.security;


import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Get Authorization header
        String authHeader =
                request.getHeader("Authorization");

        // No token
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // Remove "Bearer "
        String token = authHeader.substring(7);

        try {

            // Validate JWT
            if (jwtService.isTokenValid(token)) {

                String username =
                        jwtService.extractUsername(token);

                // Create authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                Collections.emptyList()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // Store authentication
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }

        } catch (Exception e) {

            SecurityContextHolder
                    .clearContext();
        }

        filterChain.doFilter(request, response);
    }
}