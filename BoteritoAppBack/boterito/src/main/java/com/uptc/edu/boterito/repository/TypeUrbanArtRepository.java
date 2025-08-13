package com.uptc.edu.boterito.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.uptc.edu.boterito.model.TypeUrbanArt;

@Repository
public interface TypeUrbanArtRepository extends MongoRepository<TypeUrbanArt, String>{

}
