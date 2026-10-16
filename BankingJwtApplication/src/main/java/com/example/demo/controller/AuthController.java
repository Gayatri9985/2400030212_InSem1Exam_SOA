package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.LoginRequest;
import com.example.demo.security.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Login and JWT authentication")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Operation(
        summary = "Login",
        description = "Login with username and password to generate JWT token"
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        // Demo credentials
        if ("admin".equals(request.getUsername())
                && "1234".equals(request.getPassword())) {

            String token =
                    jwtService.generateToken(request.getUsername());

            return ResponseEntity.ok(
                    Map.of(
                            "message", "Login successful",
                            "username", request.getUsername(),
                            "token", token
                    )
            );
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        Map.of(
                                "message",
                                "Invalid username or password"
                        )
                );
    }
}