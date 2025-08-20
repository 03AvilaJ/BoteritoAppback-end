package com.uptc.edu.boterito.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "usuarios")
public class User {
    @Id
    private String id;
    private String nombre;
    private String pseudonimo;
    private String fecha_nacimiento;
    private String email;
    private String biografia;
    private String password;
    private ObjectId roles_id;
    private Role role;
}
