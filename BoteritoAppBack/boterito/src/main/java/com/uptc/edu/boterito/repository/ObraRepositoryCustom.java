package com.uptc.edu.boterito.repository;

import java.util.List;

import com.uptc.edu.boterito.dto.ObraUrbanArtDTO;

public interface ObraRepositoryCustom {
    List<ObraUrbanArtDTO> findAllValidates();
    List<ObraUrbanArtDTO> findAll();
    List<ObraUrbanArtDTO> findByUsuarioCargaPseudonimo(String pseudonimo);
    
}

