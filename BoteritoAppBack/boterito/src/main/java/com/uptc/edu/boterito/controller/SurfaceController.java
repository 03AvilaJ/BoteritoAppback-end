package com.uptc.edu.boterito.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uptc.edu.boterito.model.Surface;
import com.uptc.edu.boterito.service.SurfaceService;

@RestController
@RequestMapping("api/superficies")
public class SurfaceController {

    @Autowired
    private SurfaceService surfaceService;

    @GetMapping
    public List<Surface> allSurfaces(){
        return surfaceService.allSurfaces();
    }
}