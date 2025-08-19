package com.uptc.edu.boterito.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.uptc.edu.boterito.model.Ilustracion;
import com.uptc.edu.boterito.repository.IllustrationRepository;


public class IllustrationService {
    @Autowired
    private IllustrationRepository illustrationRepository;

    public List<Ilustracion> allIllustrations(){
        return illustrationRepository.findAll();
    }
}
