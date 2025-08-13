package com.uptc.edu.boterito.controller;


import com.uptc.edu.boterito.dto.ObraRequest;
import com.uptc.edu.boterito.model.ObraUrbanArt;
import com.uptc.edu.boterito.service.ObraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/obras")
@CrossOrigin(origins = "*")
public class ObraController {

    @Autowired
    private ObraService obraService;

    @GetMapping("/listaObras")
    public List<ObraUrbanArt> findAllWithAutor() {
        return obraService.findAllWithAutor();
    }

    @GetMapping("/filtrar")
    public List<ObraUrbanArt> findFilterObras(@RequestParam String typeFilter, @RequestParam String filter) {
        return obraService.findFilterObras(typeFilter, filter);
    }

    @PostMapping("/guardarObra")
    public ObraUrbanArt registrarObra(@RequestBody ObraRequest obra) {
        return obraService.createObra(obra);
    }
}
