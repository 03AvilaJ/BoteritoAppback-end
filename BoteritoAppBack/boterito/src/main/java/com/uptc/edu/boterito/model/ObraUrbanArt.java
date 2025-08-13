package com.uptc.edu.boterito.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "obras")
public class ObraUrbanArt {
    @Id
    private String id;

    private String titulo;

    @Field("autor_id")
    private String autorId;

    private Author autor;

    @Field("fecha_creacion")
    private String fechaCreacion;

    @Field("ilustracion_id")
    private String ilustracionId;

    private Ilustracion ilustracion;

    @Field("tecnicas_id")
    private String tecnicaId;

    private Technique tecnica;

    @Field("ubicaciones_id")
    private String ubicacionId;

    private Location ubicacion;

    private String descripcion;

    @Field("estado_conservacion")
    private String estadoConservacion;

    private int likes;

    @Field("comentarios")
    private List<Comment> comentarios;

    private String mensaje;

    private String superficie;


    @Field("tipo_mural_id")
    private String tipoMuralId;

    private TypeUrbanArt tipo;
}

