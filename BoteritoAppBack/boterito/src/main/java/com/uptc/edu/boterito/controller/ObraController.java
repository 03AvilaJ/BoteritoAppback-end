package com.uptc.edu.boterito.controller;

import com.uptc.edu.boterito.dto.ObraRequest;
import com.uptc.edu.boterito.dto.ObraUrbanArtDTO;
import com.uptc.edu.boterito.model.Calification;
import com.uptc.edu.boterito.model.Comment;
import com.uptc.edu.boterito.model.UrbanArt;
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
    public List<ObraUrbanArtDTO> findAll() {
        return obraService.findAll();
    }

    @GetMapping("/listaObras")
    public List<ObraUrbanArtDTO> findAllValidates() {
        return obraService.findAllValidates();
    }

    @GetMapping("/listaObrasUsuario")
    public ResponseEntity<?> findByOneUser(@CookieValue(name = "jwt", required = false) String token) {
        try {
            if (token == null) {
                return ResponseEntity.status(401).body("No autenticado");
            }
            List<ObraUrbanArtDTO> list = obraService.findByOneUser(token);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cargar sus obra: " + e.getMessage());
        }

    }

    @PostMapping("/guardarObra")
    public ObraUrbanArtDTO registrarObra(@RequestPart("obra") ObraRequest obra, // campos del formulario
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        return obraService.createObra(obra, imagen);
    }

    @PutMapping("/updateObra")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateObra(@RequestBody ObraUrbanArtDTO obra) {
        try {
            ObraUrbanArtDTO updateObra = obraService.update(obra);
            return ResponseEntity.ok(updateObra);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar la obra: " + e.getMessage());
        }
    }

    // Para actualizar parcialmente un campo (ej: titulo)
    @PatchMapping("/{id}/validarobra")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarEstadoRegistrado(
            @PathVariable String id,
            @RequestParam String idRegisteredStatus) {
        try {
            ObraUrbanArtDTO obra = obraService.updateStatusRegister(id, idRegisteredStatus);

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

    @PostMapping("/{obraId}/like")
    public ResponseEntity<?> addLike(
            @PathVariable String obraId,
            @CookieValue(name = "jwt", required = false) String token) {
        if (token == null) {
            return ResponseEntity.status(401).body("No autenticado");
        }

        Boolean isLike = obraService.toggleLike(obraId, token);
        return ResponseEntity.ok(isLike);
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
