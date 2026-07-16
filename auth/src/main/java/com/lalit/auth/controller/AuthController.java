package com.lalit.auth.controller;

import com.lalit.auth.dto.AuthResponse;
import com.lalit.auth.dto.LoginRequest;
import com.lalit.auth.dto.RegisterRequest;
import com.lalit.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Register User",
            description = "Registers a new user account and returns a JWT token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @Operation(
            summary = "Login User",
            description = "Authenticates user credentials and returns a JWT token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(
                authService.login(request)
        );
    }
}