package com.lalit.auth.service;

import com.lalit.auth.client.UserClient;
import com.lalit.auth.dto.AuthResponse;
import com.lalit.auth.dto.LoginRequest;
import com.lalit.auth.dto.RegisterRequest;
import com.lalit.auth.dto.UserRequest;
import com.lalit.auth.entity.AuthUser;
import com.lalit.auth.enums.Role;
import com.lalit.auth.repository.AuthRepo;
import com.lalit.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepo authRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserClient userClient;

    public AuthResponse register(RegisterRequest request){
        boolean exists=authRepo
                .findByEmail(request.getEmail())
                .isPresent();

        if(exists){
            throw new RuntimeException(
                    "Email already exists"
            );
        }

        AuthUser user =AuthUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .build();

        AuthUser savedUser = authRepo.save(user);

        userClient.createUser(
                new UserRequest(
                        savedUser.getId(),
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail()
                )
        );

        String token = jwtService.generateToken(
                savedUser.getEmail(),
                savedUser.getRole().name(),
                savedUser.getId()
        );

        return new AuthResponse(token);
    }

    public AuthResponse login(
            LoginRequest request
    ){
        AuthUser user=authRepo
                .findByEmail(request.getEmail())
                .orElseThrow(()->
                        new RuntimeException("User not found")
                );

        boolean matches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if(!matches){
            throw new RuntimeException("Invalid credentials"
            );
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getId()
        );

        return new AuthResponse(token);
    }
}
