package com.uptc.edu.boterito.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "usuarios")
public class Author {
    @Id
    private String id;
    private String nombre;
    private String nacionalidad;
    private String fecha_nacimiento;
    private String biografia;
}
