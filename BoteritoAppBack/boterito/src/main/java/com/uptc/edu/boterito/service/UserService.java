package com.uptc.edu.boterito.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uptc.edu.boterito.model.Role;
import com.uptc.edu.boterito.model.User;
import com.uptc.edu.boterito.repository.RoleRepository;
import com.uptc.edu.boterito.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método para crear usuario con contraseña hasheada
    public User createUser(String name, String pseudonimo, String email, String password, String role,
            String fecha_nacimiento) {
        // Verificar si ya existe email
        User userEmail = userRepository.findByEmail(email);
        if (userEmail != null) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        // Verificar si ya existe pseudonimo
        User userpseudonimo = userRepository.findByPseudonimo(pseudonimo);
        if (userpseudonimo != null) {
            throw new IllegalArgumentException("El pseudónimo ya está en uso");
        }

        User user = new User();
        user.setNombre(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles_id(new ObjectId(role));
        user.setFecha_nacimiento(fecha_nacimiento);
        user.setPseudonimo(pseudonimo);
        return userRepository.save(user);
    }

    public List<User> allUsers() {
        return userRepository.findAllUsersWithRoles();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User savUser(User user) {
        return userRepository.save(user);
    }

    public User changeRole(String email, String id_role) {
        if (!ObjectId.isValid(id_role)) {
            throw new IllegalArgumentException("El id del rol no es válido");
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        user.setRoles_id(new ObjectId(id_role));
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        Role role = roleRepository.findById(user.getRoles_id().toString())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + role.getRol().toUpperCase())));
    }

}
