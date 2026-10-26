package com.example.digital_fit.service.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.repository.Auth.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByUsername(login)
                .orElseGet(() -> usuarioRepository.findByEmail(login)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado")));

        return User.withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getRol().name())
                .build();
    }
}
