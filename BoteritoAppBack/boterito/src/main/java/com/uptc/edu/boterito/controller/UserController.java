package com.uptc.edu.boterito.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uptc.edu.boterito.dto.UserDTO;
import com.uptc.edu.boterito.model.User;
import com.uptc.edu.boterito.service.UserService;

@RestController
@RequestMapping("/api/usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        System.out.println(user.toString());
        try {
            User newUser = userService.createUser(
                    user.getNombre(),
                    user.getPseudonimo(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getRoles_id(),
                    user.getFecha_nacimiento()
            );
            UserDTO responseUser = new UserDTO(user.getId(), user.getNombre(), user.getEmail());
            return ResponseEntity.ok(responseUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
