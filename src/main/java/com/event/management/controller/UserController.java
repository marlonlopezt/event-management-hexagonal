package com.event.management.controller;

import com.event.management.model.ApiResponse;
import com.event.management.model.Event;
import com.event.management.model.User;
import com.event.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser (@RequestBody User user){
        return userService.createUser(user);
    }
    @GetMapping
    public ResponseEntity<List<User>> getAllUser() {
        return userService.getAllUser();
    }
}
