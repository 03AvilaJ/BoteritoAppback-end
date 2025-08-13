package com.uptc.edu.boterito.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "tipo_mural")
public class TypeUrbanArt {
    @Id
    private String id;
    private String tipo_mural;
}
