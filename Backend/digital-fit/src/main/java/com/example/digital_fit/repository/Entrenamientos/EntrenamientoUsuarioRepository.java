package com.example.digital_fit.repository.Entrenamientos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.digital_fit.model.Entrenamientos.EntrenamientoUsuario;
import com.example.digital_fit.model.Enums.CategoriaEntrenamientoComunidad;
import com.example.digital_fit.model.Enums.NivelEntrenamiento;
import com.example.digital_fit.model.Auth.Usuario;

@Repository
public interface EntrenamientoUsuarioRepository extends JpaRepository<EntrenamientoUsuario, Long> {

    List<EntrenamientoUsuario> findByUsuarioId(Long usuarioId);

    List<EntrenamientoUsuario> findByUsuario(Usuario usuario);

    List<EntrenamientoUsuario> findByUsuarioAndCategoria(Usuario usuario, CategoriaEntrenamientoComunidad categoria);

    List<EntrenamientoUsuario> findByUsuarioAndNivel(Usuario usuario, NivelEntrenamiento nivel);

    List<EntrenamientoUsuario> findByUsuarioAndDuracionEnMinutosLessThanEqual(Usuario usuario,
            Integer duracionEnMinutos);

    List<EntrenamientoUsuario> findByUsuarioAndNombreContainingIgnoreCase(Usuario usuario, String nombre);

    Optional<EntrenamientoUsuario> findByUsuarioAndNombreIgnoreCase(Usuario usuario, String nombre);
}