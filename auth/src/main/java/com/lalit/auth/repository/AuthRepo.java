package com.lalit.auth.repository;

import com.lalit.auth.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepo extends JpaRepository<AuthUser,Long> {

    Optional<AuthUser> findByEmail(String email);
}
