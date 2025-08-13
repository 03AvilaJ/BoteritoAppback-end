package com.uptc.edu.boterito.dto;

import lombok.Data;

@Data
public class ObraRequest {

    private String titulo;

    private String autor_name;

    private String tecnica;

    private String fechaCreacion;

    private String descripcion;

    private String ancho;

    private String alto;

    private String mensaje;

    private String tipoMural;
    
    private String estadoConservacionId;

    private String superficieId;

    private String lat;

    private String lng;

    private String direccion;

    private String estadoRegistradoId;

    private String link_obra; 
}
