package com.lalit.user.controller;

import com.lalit.user.dto.UserRequest;
import com.lalit.user.dto.UserResponse;
import com.lalit.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get all users",
            description = "Returns all registered users"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Users fetched successfully"
    )
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUser() {
        return ResponseEntity.ok(
                userService.fetchALlUser()
        );
    }

    @Operation(
            summary = "Create user profile",
            description = "Creates a new user profile associated with an auth account"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed"
            )
    })
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserRequest userRequest
    ) {

        UserResponse user = userService.addUser(userRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }

    @Operation(
            summary = "Get user by id",
            description = "Fetches a user using user id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> userById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                userService.getById(id)
        );
    }

    @Operation(
            summary = "Update user profile",
            description = "Updates an existing user's information"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest updateUserRequest
    ) {

        return ResponseEntity.ok(
                userService.updateUser(
                        id,
                        updateUserRequest
                )
        );
    }

    @Operation(
            summary = "Delete user",
            description = "Deletes a user account"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "User deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id
    ) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}