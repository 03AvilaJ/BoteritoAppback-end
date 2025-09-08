package com.uptc.edu.boterito.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uptc.edu.boterito.model.Typography;
import com.uptc.edu.boterito.repository.TypographyRepository;

@Service
public class TypographyService {

    @Autowired
    private TypographyRepository typographyRepository;

    public List<Typography> allTypographies(){
        return typographyRepository.findAll();
    }
}
