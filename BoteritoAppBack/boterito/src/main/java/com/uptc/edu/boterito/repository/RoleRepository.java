package com.uptc.edu.boterito.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.uptc.edu.boterito.model.Role;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {

}
