package com.uptc.edu.boterito.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "ubicaciones")
public class Location {
    @Id
    private String id;
    private float lat;
    private float lng;
    private String direccion;
    private String lugar;
}
