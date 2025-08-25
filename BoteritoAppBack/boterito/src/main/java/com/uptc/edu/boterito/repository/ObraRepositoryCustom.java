package com.uptc.edu.boterito.repository;

import com.uptc.edu.boterito.model.ObraUrbanArt;
import java.util.List;

public interface ObraRepositoryCustom {
    List<ObraUrbanArt> findAllValidates();
    List<ObraUrbanArt> findAll();
    
}

