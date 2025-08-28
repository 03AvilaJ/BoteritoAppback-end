package com.uptc.edu.boterito.controller;

import com.uptc.edu.boterito.dto.ObraRequest;
import com.uptc.edu.boterito.model.Calification;
import com.uptc.edu.boterito.model.Comment;
import com.uptc.edu.boterito.model.Like;
import com.uptc.edu.boterito.model.ObraUrbanArt;
import com.uptc.edu.boterito.service.ObraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/obras")
@CrossOrigin(origins = "*")
public class ObraController {

    @Autowired
    private ObraService obraService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<ObraUrbanArt> findAll() {
        return obraService.findAll();
    }

    @GetMapping("/listaObras")
    public List<ObraUrbanArt> findAllValidates() {
        return obraService.findAllValidates();
    }

    @PostMapping("/guardarObra")
    public ObraUrbanArt registrarObra(@RequestPart("obra") ObraRequest obra, // campos del formulario
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        return obraService.createObra(obra, imagen);
    }

    // Para actualizar parcialmente un campo (ej: titulo)
    @PatchMapping("/{id}/validarobra")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarEstadoRegistrado(
            @PathVariable String id,
            @RequestParam String idRegisteredStatus) {
        try {
            ObraUrbanArt obra = obraService.updateStatusRegister(id, idRegisteredStatus);

            return ResponseEntity.ok(obra);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el estado registrado: " + e.getMessage());
        }
    }

    @PostMapping("/{obraId}/comments")
    public ResponseEntity<?> addComment(
            @PathVariable String obraId,
            @RequestBody Comment comment,
            @CookieValue(name = "jwt", required = false) String token // 👈 aquí leemos la cookie
    ) {
        if (token == null) {
            return ResponseEntity.status(401).body("No autenticado");
        }

        Comment newComment = obraService.addComment(comment, obraId, token);
        return ResponseEntity.ok(newComment);
    }

    @GetMapping("/{obraId}/like")
    public ResponseEntity<?> addLike(
            @PathVariable String obraId,
            @CookieValue(name = "jwt", required = false) String token // 👈 aquí leemos la cookie
    ) {
        if (token == null) {
            return ResponseEntity.status(401).body("No autenticado");
        }

        Like newLike = obraService.addlIKE(obraId, token);
        return ResponseEntity.ok(newLike);
    }

    @PostMapping("/{obraId}/calificacion")
    public ResponseEntity<?> addCalification(
            @PathVariable String obraId,
            @RequestBody Calification calification,
            @CookieValue(name = "jwt", required = false) String token // 👈 aquí leemos la cookie
    ) {
        if (token == null) {
            return ResponseEntity.status(401).body("No autenticado");
        }

        obraService.addCalification(calification, obraId, token);
        return ResponseEntity.ok("Calificacion agregada");
    }

}
