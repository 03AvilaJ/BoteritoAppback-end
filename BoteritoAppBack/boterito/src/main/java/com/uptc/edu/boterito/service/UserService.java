package com.uptc.edu.boterito.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uptc.edu.boterito.model.User;
import com.uptc.edu.boterito.repository.UserRepository;


@Service
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método para crear usuario con contraseña hasheada
    public User createUser(String name, String pseudonimo, String email, String password, String role, String fecha_nacimiento) {
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
    user.setRoles_id(role);
    user.setFecha_nacimiento(fecha_nacimiento);
    user.setPseudonimo(pseudonimo);
    return userRepository.save(user);
}


    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User savUser(User user){
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userSearch = userRepository.findByEmail(email);
        if (userSearch == null) {
            throw new UsernameNotFoundException("User not found");
        }
        System.out.println("User found: " + userSearch.getEmail());
        System.out.println("Password hash: " + userSearch.getPassword());
        return org.springframework.security.core.userdetails.User
                .withUsername(userSearch.getEmail())
                .password(userSearch.getPassword())
                .roles(userSearch.getRoles_id())
                .build();
    }

}

