package com.uptc.edu.boterito.model;

import org.bson.types.ObjectId;

import lombok.Data;

@Data
public class Like {
    private ObjectId usuarios_id;
    private String user_name;
}
