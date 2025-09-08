package com.uptc.edu.boterito.dto;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

import com.uptc.edu.boterito.model.Calification;
import com.uptc.edu.boterito.model.Comment;
import com.uptc.edu.boterito.model.ConservationStatus;
import com.uptc.edu.boterito.model.Illustration;
import com.uptc.edu.boterito.model.Like;
import com.uptc.edu.boterito.model.Location;
import com.uptc.edu.boterito.model.RegisteredStatus;
import com.uptc.edu.boterito.model.Surface;
import com.uptc.edu.boterito.model.Technique;
import com.uptc.edu.boterito.model.TypeUrbanArt;
import com.uptc.edu.boterito.model.Typography;
import com.uptc.edu.boterito.model.User;

@Data
@Document(collection = "obras")
public class ObraUrbanArtDTO {
    @Id
    private String id;

    private String titulo;

    @Field("fecha_creacion")
    private String fechaCreacion;

    @Field("id_usuario_carga")
    private ObjectId id_usuario_carga;

    private UserDTO owner_user;

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

    private Illustration ilustracion;

    @Field("tecnicas_id")
    private ObjectId tecnicaId;

    private Technique tecnica;

    @Field("tipo_mural_id")
    private ObjectId tipoMuralId;

    private TypeUrbanArt tipo;

    @Field("ubicaciones_id")
    private ObjectId ubicacionId;

    private Location ubicacion;

    private String ancho;

    private String alto;

    private String observaciones;

    @Field("estado_conservacion_id")
    private ObjectId estadoConservacionId;

    private ConservationStatus estadoConservacion;

    private String autor_name;

    @Field("tipografias_id")
    private ObjectId tipografiasId;

    private Typography typography;

    @Field("superficie_id")
    private ObjectId superficieId;

    private Surface surface;

    private String contexto_historico;

    private String restaurador;

    @Field("estado_registrado_id")
    private ObjectId estadoRegistradoId;

    private RegisteredStatus registeredStatus;

    private String link_obra;
        
}

