package com.uptc.edu.boterito.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.uptc.edu.boterito.config.GoogleConfig;
import com.uptc.edu.boterito.model.User;
import com.uptc.edu.boterito.security.JwtUtil;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Collections;
import java.util.Map;

@Service
public class GoogleAuthService {

    @Autowired
    private GoogleConfig googleConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    public Map<String, Object> authenticateWithGoogle(String token, HttpServletResponse response) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleConfig.getGoogleClientId()))
                .build();

        GoogleIdToken idToken = verifier.verify(token);
        if (idToken == null) {
            throw new Exception("Token de Google inválido");
        }

        Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        String sub = payload.getSubject();
        String name = (String) payload.get("name");
        
        User user = userService.findByEmail(email);

        if (user == null) {
            user = userService.createUser(name, email, email,"","689bd2e00691edc2fc5831fd","");
        }

        UserDetails userDetails = userService.loadUserByUsername(email);

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(auth -> auth.getAuthority().replace("ROLE_", "")) // quitamos el prefijo
                .orElse("USER"); // valor por defecto

        String jwt = jwtUtil.generateToken(user.getPseudonimo(), user.getId(), role);
        
        // Cookie
        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .secure(false) // en prod -> true
                .path("/")
                .maxAge(60 * 60)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return Map.of(
                "message", "Login con Google exitoso",
                "role", role,
                "pseudonimo", user.getPseudonimo()
        );
    }
}

