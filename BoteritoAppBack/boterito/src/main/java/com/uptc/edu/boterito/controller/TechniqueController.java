package com.uptc.edu.boterito.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uptc.edu.boterito.model.Technique;
import com.uptc.edu.boterito.service.TechniqueService;

@RestController
@RequestMapping("/api/tecnicas")
public class TechniqueController {

    @Autowired
    private TechniqueService techniqueService;

    @GetMapping
    public List<Technique> allTechniques(){
        return techniqueService.allTechniques();
    }
}
