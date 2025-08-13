package com.uptc.edu.boterito.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uptc.edu.boterito.model.ConservationStatus;
import com.uptc.edu.boterito.repository.ConservationStatusRepository;

@Service
public class ConservationStatusService {

    @Autowired
    private ConservationStatusRepository conservationStatusRepository;

    public List<ConservationStatus> allConservationStatus(){
        return conservationStatusRepository.findAll();
    }
}
