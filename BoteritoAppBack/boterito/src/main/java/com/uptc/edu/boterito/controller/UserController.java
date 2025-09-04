package com.uptc.edu.boterito.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uptc.edu.boterito.dto.UserDTO;
import com.uptc.edu.boterito.model.User;
import com.uptc.edu.boterito.security.JwtUtil;
import com.uptc.edu.boterito.service.UserService;

@RestController
@RequestMapping("/api/usuarios")
public class UserController {

    private static final String ID_USER_ROLE = "689bd2e00691edc2fc5831fd";

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

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
public ResponseEntity<?> searchUser(@CookieValue(name = "jwt", required = false) String token) {
    if (token == null) {
        return ResponseEntity.status(401).body("No autenticado");
    }

    try {
        String pseudonimo = jwtUtil.extractPseudonimo(token); // el "subject" del JWT
        User user = userService.findByPseudonimo(pseudonimo);
        
        if (user == null) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
        return ResponseEntity.ok(user);
    } catch (Exception e) {
        return ResponseEntity.status(401).body("Token inválido o expirado");
    }
}


}
