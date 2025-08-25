package com.uptc.edu.boterito.model;

import org.bson.types.ObjectId;

import lombok.Data;

@Data
public class Calification {
    private ObjectId usuarios_id;
    private String valor;
    private String user_name;

}
