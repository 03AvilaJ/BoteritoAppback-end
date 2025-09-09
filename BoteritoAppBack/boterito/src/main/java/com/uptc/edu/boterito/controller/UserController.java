package com.uptc.edu.boterito.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;


import com.uptc.edu.boterito.dto.UserDTO;
import com.uptc.edu.boterito.model.User;
import com.uptc.edu.boterito.security.JwtUtil;
import com.uptc.edu.boterito.service.EmailService;
import com.uptc.edu.boterito.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@RestController
@RequestMapping("/api/usuarios")
public class UserController {

    private static final String ID_USER_ROLE = "689bd2e00691edc2fc5831fd";

    @Value("${frontend.url}")   // Inyecta la URL desde properties
    private String frontendUrl;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

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
    @GetMapping("/roles")
    public ResponseEntity<?> allRoles() {
        try {
            return ResponseEntity.ok(userService.allRoles());
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

    @PutMapping("/update")
    public ResponseEntity<?> updatePerfil(
            @CookieValue(name = "jwt", required = false) String token,
            @RequestBody User updatedData) {
        if (token == null) {
            return ResponseEntity.status(401).body("No autenticado");
        }

        try {
            // 1. Extraer pseudónimo (o id) desde el token
            String pseudonimo = jwtUtil.extractPseudonimo(token);

            // 2. Buscar el usuario en la BD
            User user = userService.findByPseudonimo(pseudonimo);
            if (user == null) {
                return ResponseEntity.status(404).body("Usuario no encontrado");
            }

            // 3. Actualizar los campos (solo los que quieras permitir modificar)
            if (updatedData.getBiografia() != null)
                user.setBiografia(updatedData.getBiografia());

            // 4. Guardar cambios en la BD
            userService.savUser(user);

            // 5. Responder con el usuario actualizado
            return ResponseEntity.ok(user);

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token inválido o expirado");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        User user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        // Generar token de recuperación
        String resetToken = Jwts.builder()
                .setSubject(user.getEmail())
                .claim("type", "password_reset")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000)) // 15 min
                .signWith(jwtUtil.getKey())
                .compact();

        // Crear link de recuperación
        String resetLink = frontendUrl + "/reset-password?token=" + resetToken;

        // Enviar correo
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);

        return ResponseEntity.ok("Se envió un correo de recuperación a " + user.getEmail());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        try {
            // 1. Validar token
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(jwtUtil.getKey())
                    .build()
                    .parseClaimsJws(token);

            // 2. Verificar que sea de tipo reset
            if (!"password_reset".equals(claims.getBody().get("type", String.class))) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido");
            }

            // 3. Obtener el email
            String email = claims.getBody().getSubject();

            // 4. Buscar el usuario por email
            User user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }

            // 5. Actualizar la contraseña (encriptada)
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.savUser(user);

            return ResponseEntity.ok("Contraseña actualizada correctamente ✅");

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El token expiró");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido");
        }
    }

}
