package com.uptc.edu.boterito.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "tecnicas")
public class Technique {
    @Id
    private String id;
    private String nombre;
    private String descripcion;
}
