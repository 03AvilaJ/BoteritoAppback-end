package com.uptc.edu.boterito.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uptc.edu.boterito.model.Typography;
import com.uptc.edu.boterito.service.TypographyService;

@RestController
@RequestMapping("/api/tipografias")
public class TypographyController {

    @Autowired
    private TypographyService typographyService;

    @GetMapping
    public List<Typography> allTypographies(){
        return typographyService.allTypographies();
    }
}
