package com.uptc.edu.boterito.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("estado_registro")
public class RegisteredStatus {
    @Id
    private String id;
    private String estado_registro;
}
