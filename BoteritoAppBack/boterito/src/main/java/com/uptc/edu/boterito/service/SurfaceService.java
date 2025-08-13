package com.uptc.edu.boterito.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uptc.edu.boterito.model.Surface;
import com.uptc.edu.boterito.repository.SurfaceRepository;

@Service
public class SurfaceService {

    @Autowired
    private SurfaceRepository surfaceRepository;

    public List<Surface> allSurfaces(){
        return surfaceRepository.findAll();
    }
}
