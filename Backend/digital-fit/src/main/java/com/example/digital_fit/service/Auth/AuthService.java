package com.example.digital_fit.service.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.digital_fit.dto.Admin.CrearAdmin;
import com.example.digital_fit.dto.Auth.UsuarioDTO;
import com.example.digital_fit.exception.EmailYaExisteException;
import com.example.digital_fit.exception.UsernameYaExiste;
import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.Enums.Rol;
import com.example.digital_fit.repository.Auth.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Registrar usuario:
    @Transactional
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

    // Registrar admin:
    @Transactional
    public void registrarAdmin(CrearAdmin crearAdmin) {

        if (usuarioRepository.findByUsername(crearAdmin.getUsername()).isPresent()) {
            throw new UsernameYaExiste("Nombre de usuario ya existe");
        }

        Usuario admin = new Usuario();

        admin.setUsername(crearAdmin.getUsername());
        admin.setPassword(passwordEncoder.encode(crearAdmin.getPassword()));
        admin.setRol(Rol.ADMIN);

        usuarioRepository.save(admin);
    }

    
}
