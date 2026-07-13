package com.lalit.order.security;

public record UserPrincipal(
        Long userId,
        String email,
        String role
) {

}
