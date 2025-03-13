package com.event.management.service;

import com.event.management.model.ApiResponse;
import com.event.management.model.Event;
import com.event.management.model.User;
import com.event.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ResponseEntity<ApiResponse<User>> createUser (User user){
        try{
            User newUser = userRepository.save(user);
            ApiResponse<User> response = new ApiResponse<>(newUser, "Usuario creado.");
            return  ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Error al crear usuario."));
        }
    }
    public ResponseEntity<List<User>> getAllUser() {
        List<User> user = userRepository.findAll();
        return ResponseEntity.ok(user);
    }
}
