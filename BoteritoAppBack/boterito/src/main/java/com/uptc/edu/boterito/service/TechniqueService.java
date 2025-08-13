package com.uptc.edu.boterito.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uptc.edu.boterito.model.Technique;
import com.uptc.edu.boterito.repository.TechniqueRepository;

@Service
public class TechniqueService {

    @Autowired
    private TechniqueRepository techniqueRepository;

    public List<Technique> allTechniques(){
        return techniqueRepository.findAll();
    }
}
