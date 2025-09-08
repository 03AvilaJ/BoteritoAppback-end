package com.uptc.edu.boterito.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uptc.edu.boterito.model.Illustration;
import com.uptc.edu.boterito.repository.IllustrationRepository;

@Service
public class IllustrationService {

    @Autowired
    private IllustrationRepository illustrationRepository;

    public List<Illustration> allIllustrations(){
        return illustrationRepository.findAll();
    }
}
