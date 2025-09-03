package com.uptc.edu.boterito.repository;

import java.util.List;

import com.uptc.edu.boterito.model.User;

public interface UserRepositoryCustom {
    List<User> findAllUsersWithRoles();
    User findByPseudonimo(String pseudonimo);
}
