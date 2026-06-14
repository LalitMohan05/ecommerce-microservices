package com.lalit.auth.client;

import com.lalit.auth.dto.UserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "USER-SERVICE"
)
public interface UserClient {
    @PostMapping("/api/users")
    void createUser(
            @RequestBody UserRequest request
    );
}
