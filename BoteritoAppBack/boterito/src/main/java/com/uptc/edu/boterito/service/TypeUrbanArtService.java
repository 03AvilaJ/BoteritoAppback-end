package com.uptc.edu.boterito.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uptc.edu.boterito.model.TypeUrbanArt;
import com.uptc.edu.boterito.repository.TypeUrbanArtRepository;

@Service
public class TypeUrbanArtService {

    @Autowired
    private TypeUrbanArtRepository typeUrbanArtRepository;

    public List<TypeUrbanArt> allTypeUrbanArt(){
        return typeUrbanArtRepository.findAll();
    }
}
