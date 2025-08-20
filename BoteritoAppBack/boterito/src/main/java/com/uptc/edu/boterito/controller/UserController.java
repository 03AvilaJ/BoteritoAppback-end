package com.uptc.edu.boterito.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    private static final String ID_USER_ROLE = "689bd2e00691edc2fc5831fd";

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            userService.createUser(
                    user.getNombre(),
                    user.getPseudonimo(),
                    user.getEmail(),
                    user.getPassword(),
                    ID_USER_ROLE,
                    user.getFecha_nacimiento());
            UserDTO responseUser = new UserDTO(user.getId(), user.getNombre(), user.getEmail());
            return ResponseEntity.ok(responseUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/updaterole")
    public ResponseEntity<?> updateRole(@RequestBody User user) {
        try {
            userService.changeRole(user.getEmail(), user.getRoles_id().toString());
            UserDTO responseUser = new UserDTO(user.getId(), user.getNombre(), user.getEmail());
            return ResponseEntity.ok(responseUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public List<User> allUser() {
        return userService.allUsers();
    }

    @GetMapping("/perfil")
    public ResponseEntity<User> searchUser(Authentication authentication) {
        String email = authentication.getName(); // se obtiene del JWT o cookie
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

}
