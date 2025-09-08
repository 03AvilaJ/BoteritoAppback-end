package com.uptc.edu.boterito.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "obras")
public class UrbanArt {

    @Id
    private String id;

    private String titulo;

    @Field("fecha_creacion")
    private String fechaCreacion;

    @Field("id_usuario_carga")
    private ObjectId id_usuario_carga;

    @Field("fecha_registro")
    private String fecha_registro;

    private String descripcion;

    private List<Like> likes = new ArrayList<>();

    private List<Calification> calificaciones = new ArrayList<>();

    @Field("comentarios")
    private List<Comment> comentarios = new ArrayList<>();

    private String mensaje;

    @Field("ilustracion_id")
    private ObjectId ilustracionId;

    @Field("tecnicas_id")
    private ObjectId tecnicaId;

    @Field("tipo_mural_id")
    private ObjectId tipoMuralId;

    @Field("ubicaciones_id")
    private ObjectId ubicacionId;

    private Location ubicacion;

    private String ancho;

    private String alto;

    private String observaciones;

    @Field("estado_conservacion_id")
    private ObjectId estadoConservacionId;

    private String autor_name;

    @Field("tipografias_id")
    private ObjectId tipografiasId;


    @Field("superficie_id")
    private ObjectId superficieId;


    private String contexto_historico;

    private String restaurador;

    @Field("estado_registrado_id")
    private ObjectId estadoRegistradoId;

    private String link_obra;
        




}
