package com.uptc.edu.boterito.repository;

import com.uptc.edu.boterito.model.ObraUrbanArt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObraRepository extends MongoRepository<ObraUrbanArt, String>, ObraRepositoryCustom {
}