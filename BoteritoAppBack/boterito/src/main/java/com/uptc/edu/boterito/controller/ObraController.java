package com.uptc.edu.boterito.controller;


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
    private ObraService service;

    @GetMapping("/listaObras")
    public List<ObraUrbanArt> findAllWithAutor() {
        return service.findAllWithAutor();
    }

    @GetMapping("/filtrar")
    public List<ObraUrbanArt> findFilterObras(@RequestParam String typeFilter, @RequestParam String filter) {
        return service.findFilterObras(typeFilter, filter);
    }

    @PostMapping("/guardarObra")
    public ObraUrbanArt registrarObra(@RequestBody ObraUrbanArt obra) {
        return service.guardar(obra);
    }
}
