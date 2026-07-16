package com.lalit.order.config;

import com.lalit.order.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private  final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth->auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        //everyone
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/orders/**"
                        )
                        .hasAnyRole("CUSTOMER","ADMIN")

                        //admin
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/orders/**"
                        )
                        .hasRole("CUSTOMER")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/orders/*/status"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/orders/**"
                        )
                        .hasRole("CUSTOMER")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/orders/**"
                        )
                        .hasRole("CUSTOMER")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();


    }
}
