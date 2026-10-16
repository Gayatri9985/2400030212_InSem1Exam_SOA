package com.example.demo.controller;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/account")
@Tag(
    name = "Account",
    description = "Protected banking account APIs"
)
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    @Operation(
        summary = "Get account details",
        description = "Returns account details only when a valid JWT token is provided"
    )
    @GetMapping("/details")
    public Map<String, Object> getAccountDetails(
            Authentication authentication) {

        return Map.of(
            "message", "Access granted",
            "username", authentication.getName(),
            "accountNumber", "XXXX-XXXX-1234",
            "accountType", "Savings",
            "balance", 50000
        );
    }
}