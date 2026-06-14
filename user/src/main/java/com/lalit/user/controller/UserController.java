package com.lalit.user.controller;


import com.lalit.user.dto.UserRequest;
import com.lalit.user.dto.UserResponse;
import com.lalit.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<UserResponse>> getAllUser(){
        return ResponseEntity.ok(userService.fetchALlUser());
    }

    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest){
        userService.addUser(userRequest);
        return ResponseEntity.ok("User added successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> userById(@PathVariable Long id){

        return userService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id,
                                              @RequestBody UserRequest updateUserRequest){
        boolean updated= userService.updateUser(id,updateUserRequest);

        if(updated) {
            return ResponseEntity.ok("User updated successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
