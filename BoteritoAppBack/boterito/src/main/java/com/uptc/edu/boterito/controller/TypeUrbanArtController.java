package com.uptc.edu.boterito.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uptc.edu.boterito.model.TypeUrbanArt;
import com.uptc.edu.boterito.service.TypeUrbanArtService;

@RestController
@RequestMapping("/api/tipos")
public class TypeUrbanArtController {

    @Autowired
    private TypeUrbanArtService typeUrbanArtService;

    @GetMapping
    public List<TypeUrbanArt> allTypeUrbanArt(){
        return typeUrbanArtService.allTypeUrbanArt();
    }
}
