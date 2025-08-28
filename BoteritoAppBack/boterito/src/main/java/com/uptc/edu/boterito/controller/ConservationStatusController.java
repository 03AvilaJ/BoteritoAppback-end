package com.uptc.edu.boterito.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uptc.edu.boterito.model.ConservationStatus;
import com.uptc.edu.boterito.service.ConservationStatusService;

@RestController
@RequestMapping("api/conservacion")
public class ConservationStatusController {

    @Autowired
    private ConservationStatusService conservationStatusService;

    @GetMapping
    public List<ConservationStatus> allConservationStatus(){
        return conservationStatusService.allConservationStatus();
    }
}