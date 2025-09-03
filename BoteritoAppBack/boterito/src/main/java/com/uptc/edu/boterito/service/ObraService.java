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
import com.uptc.edu.boterito.security.JwtUtil;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.uptc.edu.boterito.model.Location;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private JwtUtil jwtUtil;

    public List<ObraUrbanArt> findAllValidates() {
        List<ObraUrbanArt> obras = obraRepository.findAllValidates();
        for (ObraUrbanArt obra : obras) {
            for (Comment comentario : obra.getComentarios()) {
                User usuario = userRepository.findById(comentario.getUsuarios_id().toString()).orElseThrow(
                        () -> new RuntimeException("Usuario no encontrado: " + comentario.getUsuarios_id()));
                comentario.setNameUser(usuario.getNombre());
            }
            for (Like like : obra.getLikes()) {
                User usuario = userRepository.findById(like.getUsuarios_id().toString())
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + like.getUsuarios_id()));
                like.setUser_name(usuario.getNombre());
            }
            for (Calification calification : obra.getCalificaciones()) {
                User usuario = userRepository.findById(calification.getUsuarios_id().toString()).orElseThrow(
                        () -> new RuntimeException("Usuario no encontrado: " + calification.getUsuarios_id()));
                calification.setUser_name(usuario.getNombre());
            }
        }
        return obras;
    }

    public List<ObraUrbanArt> findAll() {
        List<User> allUsers = userRepository.findAll();
        Map<String, User> userMap = allUsers.stream()
            .collect(Collectors.toMap(u -> u.getId().toString(), u -> u));

        List<ObraUrbanArt> obras = obraRepository.findAll();
        for (ObraUrbanArt obra : obras) {
            for (Comment comentario : obra.getComentarios()) {
                User usuario = userMap.get(comentario.getUsuarios_id().toString());
                comentario.setNameUser(usuario != null ? usuario.getNombre() : "Desconocido");
            }
            for (Like like : obra.getLikes()) {
                User usuario = userMap.get(like.getUsuarios_id().toString());
                like.setUser_name(usuario != null ? usuario.getNombre() : "Desconocido");
            }
            for (Calification calification : obra.getCalificaciones()) {
                User usuario = userMap.get(calification.getUsuarios_id().toString());
                calification.setUser_name(usuario != null ? usuario.getNombre() : "Desconocido");
            }
        }
        return obras;
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

    public ObraUrbanArt updateStatusRegister(String id, String idRegisterStatus) {
        if (!ObjectId.isValid(idRegisterStatus)) {
            throw new IllegalArgumentException("El idRegisterStatus no es válido");
        }

        ObraUrbanArt obra = obraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obra con id " + id + " no encontrada"));

        obra.setEstadoRegistradoId(new ObjectId(idRegisterStatus));
        return obraRepository.save(obra);
    }

    public Comment addComment(Comment comment, String obraId, String token) {
        // Validar campos obligatorios
        if (comment.getTexto() == null || comment.getTexto().trim().isEmpty()) {
            throw new IllegalArgumentException("El comentario no puede estar vacío");
        }

        // 1. Decodificar token
        String userId = jwtUtil.getUserIdFromToken(token);
        String username = jwtUtil.extractUsername(token);

        // 2. Buscar obra
        ObraUrbanArt urbanArt = obraRepository.findById(obraId)
                .orElseThrow(() -> new RuntimeException("Obra no encontrada"));

        // 3. Completar el comentario con info del usuario
        comment.setUsuarios_id(new ObjectId(userId));
        comment.setNameUser(username);
        comment.setFecha(new Date()); // forzamos fecha del servidor

        // 4. Agregar a la obra
        urbanArt.getComentarios().add(comment);

        // 5. Guardar todo
        obraRepository.save(urbanArt);
        return comment;
    }

    public Like addlIKE( String obraId, String token) {

        // 1. Decodificar token
        String userId = jwtUtil.getUserIdFromToken(token);
        String username = jwtUtil.extractUsername(token);

        // 2. Buscar obra
        ObraUrbanArt urbanArt = obraRepository.findById(obraId)
                .orElseThrow(() -> new RuntimeException("Obra no encontrada"));

        // 3. Revisar si ya existe un like de este usuario
    boolean alreadyLiked = urbanArt.getLikes().stream()
            .anyMatch(l -> l.getUsuarios_id().toHexString().equals(userId));

    if (alreadyLiked) {
        throw new RuntimeException("El usuario ya dio like a esta obra");
    }
        
        // 4. Completar con info del usuario
        Like like = new Like();
        like.setUsuarios_id(new ObjectId(userId));
        like.setUser_name(username);// forzamos fecha del servidor

        // 5. Agregar a la obra
        urbanArt.getLikes().add(like);

        // 6. Guardar todo
        obraRepository.save(urbanArt);
        return like;
    }

    public void addCalification(Calification calification, String obraId, String token) {
        // Validar campos obligatorios
        if (calification.getValor() == null || calification.getValor().trim().isEmpty()) {
            throw new IllegalArgumentException("El comentario no puede estar vacío");
        }

        // 1. Decodificar token
        String userId = jwtUtil.getUserIdFromToken(token);
        String username = jwtUtil.extractUsername(token);

        // 2. Buscar obra
        ObraUrbanArt urbanArt = obraRepository.findById(obraId)
                .orElseThrow(() -> new RuntimeException("Obra no encontrada"));

        // 3. Completar el comentario con info del usuario
        calification.setUsuarios_id(new ObjectId(userId));
        calification.setUser_name(username); // forzamos fecha del servidor

        // 4. Agregar a la obra
        urbanArt.getCalificaciones().add(calification);

        // 5. Guardar todo
        obraRepository.save(urbanArt);
    }

}
