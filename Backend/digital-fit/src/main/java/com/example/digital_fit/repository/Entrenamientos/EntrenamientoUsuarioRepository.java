package com.example.digital_fit.repository.Entrenamientos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.digital_fit.model.Entrenamientos.EntrenamientoUsuario;
import com.example.digital_fit.model.Auth.Usuario;

@Repository
public interface EntrenamientoUsuarioRepository extends JpaRepository<EntrenamientoUsuario, Long> {

    List<EntrenamientoUsuario> findByUsuarioId(Long usuarioId);

    List<EntrenamientoUsuario> findByUsuario(Usuario usuario);
}
