package com.uptc.edu.boterito.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uptc.edu.boterito.model.Illustration;
import com.uptc.edu.boterito.service.IllustrationService;

@RestController
@RequestMapping("api/ilustraciones")
public class IllustrationController {

    @Autowired
    private IllustrationService IllustrationService;

    @GetMapping
    public List<Illustration> allIllustracions(){
        return IllustrationService.allIllustrations();
    }
}
