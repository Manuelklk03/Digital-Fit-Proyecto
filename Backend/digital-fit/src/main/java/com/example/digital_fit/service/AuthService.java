package com.example.digital_fit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.digital_fit.dto.UsuarioDTO;
import com.example.digital_fit.exception.EmailYaExisteException;
import com.example.digital_fit.exception.UsernameYaExiste;
import com.example.digital_fit.model.Usuario;
import com.example.digital_fit.model.Enums.Rol;
import com.example.digital_fit.repository.UsuarioRepository;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registrar(UsuarioDTO usuarioDTO) {

        if (usuarioRepository.findByUsername(usuarioDTO.getUsername()).isPresent()) {
            throw new UsernameYaExiste("Nombre de usuario ya existe");
        }

        if (usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
            throw new EmailYaExisteException("Email ya existe");
        }

        Usuario usuario = Usuario.builder()
                .username(usuarioDTO.getUsername())
                .email(usuarioDTO.getEmail())
                .password(passwordEncoder.encode(usuarioDTO.getPassword()))
                .rol(Rol.USER)
                .build();

        usuarioRepository.save(usuario);
    }
}
