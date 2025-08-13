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
import org.springframework.web.multipart.MultipartFile;

import com.uptc.edu.boterito.model.Location;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ObraService {

    @Autowired
    private ObraRepository obraRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired 
    private CloudinaryService cloudinaryService;

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

    public ObraUrbanArt createObra(ObraRequest obra, MultipartFile imagen) {
        ObraUrbanArt urbanArt = new ObraUrbanArt();
        Location newLocation = new Location();
        newLocation.setLat(Double.parseDouble(obra.getLat()));
        newLocation.setLng(Double.parseDouble(obra.getLng()));
        newLocation.setDireccion(obra.getDireccion());
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
        urbanArt.setUbicacionId(new ObjectId(location.getId()));
        urbanArt.setEstadoRegistradoId(new ObjectId(obra.getEstadoRegistradoId()));

        // Guardar imagen en carpeta 'uploads' y asignar link_obra
    if (imagen != null && !imagen.isEmpty()) {
        File file;
        try {
            file = File.createTempFile("temp", imagen.getOriginalFilename());
            imagen.transferTo(file);
            Map uploadResult = cloudinaryService.uploadFile(file);
            String urlImagen = (String) uploadResult.get("secure_url");
            urbanArt.setLink_obra(urlImagen);

        file.delete(); // Limpiar archivo temporal
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

        
    }
        return obraRepository.save(urbanArt);
    }
}
