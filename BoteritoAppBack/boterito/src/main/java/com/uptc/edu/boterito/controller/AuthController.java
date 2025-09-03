package com.uptc.edu.boterito.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.uptc.edu.boterito.model.AuthRequest;
import com.uptc.edu.boterito.model.User;
import com.uptc.edu.boterito.security.JwtUtil;
import com.uptc.edu.boterito.service.GoogleAuthService;
import com.uptc.edu.boterito.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private GoogleAuthService googleAuthService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest loginRequest, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        // 🔎 Cargar user desde la DB
        UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());

        // 🔎 Extraer rol (asumo un solo rol por usuario)
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(auth -> auth.getAuthority().replace("ROLE_", "")) // quitamos el prefijo
                .orElse("USER"); // valor por defecto

        User user = userService.findByEmail(loginRequest.getEmail());

        // 🔑 Generar token con email y rol
        String token = jwtUtil.generateToken(user.getPseudonimo(), user.getId(), role);

        // 🍪 Crear cookie segura
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true) // 🔒 JavaScript NO puede leerla
                .secure(false) // 🔒 Solo viaja por HTTPS (en local puedes poner false)
                .path("/") // Disponible en toda la app
                .maxAge(60 * 60) // 1 hora
                .sameSite("Lax") // 🔒 Previene CSRF básico
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(Map.of("message", "Login exitoso", "role", role, "pseudonimo", user.getPseudonimo()));
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> body, HttpServletResponse response) {
        try {
            String token = body.get("token");
            Map<String, Object> result = googleAuthService.authenticateWithGoogle(token, response);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0) // 👈 Expira inmediatamente
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(Map.of("message", "Logout exitoso"));
    }

}
