package com.uptc.edu.boterito.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "tipografias")
public class Typography {
    @Id
    private String id;

    private String tipografia;
}
