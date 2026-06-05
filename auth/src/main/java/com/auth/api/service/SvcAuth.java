package com.auth.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.api.entity.User;
import com.auth.api.repository.RepoUser;
import com.auth.config.JwtUtil;

@Service
public class SvcAuth {

    @Autowired
    private RepoUser repo;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String login(String username, String password) {
        User user = repo.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        if (user.getStatus() == 0) {
            throw new RuntimeException("Usuario inactivo");
        }

        return jwtUtil.generateToken(user.getUser_id(), user.getUsername(), user.getRole());
    }

    public String register(String username, String password, String role) {
        if (repo.findByUsername(username) != null) {
            throw new RuntimeException("El usuario ya existe");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setRole(role);
        user.setStatus(1);
        repo.save(user);

        return "Usuario registrado correctamente";
    }
}