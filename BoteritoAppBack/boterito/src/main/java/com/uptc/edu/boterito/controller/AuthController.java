package com.uptc.edu.boterito.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.uptc.edu.boterito.model.AuthRequest;

import com.uptc.edu.boterito.security.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest loginRequest, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        String token = jwtUtil.generateToken(loginRequest.getEmail());

        // Crear cookie segura
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true) // 🔒 JavaScript NO puede leerla
                .secure(true) // 🔒 Solo viaja por HTTPS (en local puedes poner false)
                .path("/") // Disponible en toda la app
                .maxAge(60 * 60) // 1 hora
                .sameSite("Strict") // 🔒 Previene CSRF básico
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // Opcional: puedes devolver un body con datos del usuario
        return ResponseEntity.ok(Map.of("message", "Login exitoso"));
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
