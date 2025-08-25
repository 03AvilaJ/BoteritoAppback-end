package com.uptc.edu.boterito.model;

import java.util.Date;

import org.bson.types.ObjectId;

import lombok.Data;

@Data
public class Comment {
    private ObjectId usuarios_id;
    private String texto;
    private Date fecha;
    private String nameUser;

    // Getters y Setters
}
