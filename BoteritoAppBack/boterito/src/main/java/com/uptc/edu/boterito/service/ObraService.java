package com.uptc.edu.boterito.service;


import com.uptc.edu.boterito.model.ObraUrbanArt;
import com.uptc.edu.boterito.repository.ObraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObraService {

    @Autowired
    private ObraRepository obraRepository;

    public List<ObraUrbanArt> findAllWithAutor() {
        return obraRepository.findAllWithAutor();
    }

    public List<ObraUrbanArt> findFilterObras(String typefilter, String filter) {
        return obraRepository.filterObra(typefilter, filter);
    }

    public ObraUrbanArt guardar(ObraUrbanArt obra) {
        return obraRepository.save(obra);
    }
}
