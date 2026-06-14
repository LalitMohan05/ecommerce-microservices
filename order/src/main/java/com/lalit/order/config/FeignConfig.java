package com.lalit.order.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FeignConfig {

    private final HttpServletRequest request;

    @Bean
    public RequestInterceptor requestInterceptor(){

        return requestTemplate -> {

            String authHeader = request.getHeader("Authorization");

            if (authHeader != null) {
                requestTemplate.header("Authorization",authHeader);
            }
        };

    }

}
