package com.uptc.edu.boterito.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "ilustracion_muralista")
public class Illustration {
    @Id
    private String id;
    private String ilustracion;

}
