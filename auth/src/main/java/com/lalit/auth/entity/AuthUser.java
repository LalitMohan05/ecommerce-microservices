package com.lalit.auth.entity;

import com.lalit.auth.enums.Role;
import jakarta.persistence.*;
import lombok.*;


@Entity(name = "auth_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

}
