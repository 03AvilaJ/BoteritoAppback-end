package com.uptc.edu.boterito.service;


import com.uptc.edu.boterito.dto.ObraRequest;
import com.uptc.edu.boterito.model.Calification;
import com.uptc.edu.boterito.model.Comment;
import com.uptc.edu.boterito.model.Like;
import com.uptc.edu.boterito.model.ObraUrbanArt;
import com.uptc.edu.boterito.model.User;
import com.uptc.edu.boterito.repository.LocationRepository;
import com.uptc.edu.boterito.repository.ObraRepository;
import com.uptc.edu.boterito.repository.UserRepository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uptc.edu.boterito.model.Location;
import java.util.List;

@Service
public class ObraService {

    @Autowired
    private ObraRepository obraRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    public List<ObraUrbanArt> findAllWithAutor() {
        List<ObraUrbanArt> obras = obraRepository.findAllWithAutor();
        for (ObraUrbanArt obra : obras) {
            for (Comment comentario : obra.getComentarios()) {
                User usuario = userRepository.findById(comentario.getUsuarios_id()).orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + comentario.getUsuarios_id()));
                comentario.setNameUser(usuario.getNombre());
            }
            for (Like like : obra.getLikes()) {
                User usuario = userRepository.findById(like.getUsuarios_id()).orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + like.getUsuarios_id()));
                like.setUser_name(usuario.getNombre());
            }
            for (Calification calification : obra.getCalificaciones()) {
                User usuario = userRepository.findById(calification.getUsuarios_id()).orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + calification.getUsuarios_id()));
                calification.setUser_name(usuario.getNombre());
            }
        }
        return obras;
    }

    public List<ObraUrbanArt> findFilterObras(String typefilter, String filter) {
        return obraRepository.filterObra(typefilter, filter);
    }

    public ObraUrbanArt createObra(ObraRequest obra) {
        ObraUrbanArt urbanArt = new ObraUrbanArt();
        Location newLocation = new Location();
        newLocation.setLat(Double.parseDouble(obra.getLat()));
        newLocation.setLng(Double.parseDouble(obra.getLng()));
        Location location = locationRepository.save(newLocation);

        urbanArt.setTitulo(obra.getTitulo());
        urbanArt.setAutor_name(obra.getAutor_name());
        urbanArt.setTecnicaId(new ObjectId(obra.getTecnica()));
        urbanArt.setFechaCreacion(obra.getFechaCreacion());
        urbanArt.setDescripcion(obra.getDescripcion());
        urbanArt.setAlto(obra.getAlto());
        urbanArt.setAncho(obra.getAncho());
        urbanArt.setMensaje(obra.getMensaje());
        urbanArt.setTipoMuralId(new ObjectId(obra.getTipoMural()));
        urbanArt.setEstadoConservacionId(new ObjectId(obra.getEstadoConservacionId()));
        urbanArt.setSuperficieId(new ObjectId(obra.getSuperficieId()));
        urbanArt.setLink_obra(obra.getLink_obra());
        urbanArt.setUbicacionId(new ObjectId(location.getId()));
        return obraRepository.save(urbanArt);
    }
}
