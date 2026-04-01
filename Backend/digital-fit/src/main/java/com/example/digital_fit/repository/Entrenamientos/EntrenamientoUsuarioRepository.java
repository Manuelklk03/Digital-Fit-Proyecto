package com.example.digital_fit.repository.Entrenamientos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.Entrenamientos.EntrenamientoUsuario;
import com.example.digital_fit.model.Enums.CategoriaEntrenamientoComunidad;
import com.example.digital_fit.model.Enums.NivelEntrenamiento;

@Repository
public interface EntrenamientoUsuarioRepository extends JpaRepository<EntrenamientoUsuario, Long> {

    List<EntrenamientoUsuario> findByUsuarioId(Long usuarioId);

    List<EntrenamientoUsuario> findByUsuario(Usuario usuario);

    List<EntrenamientoUsuario> findByUsuarioAndCategoria(Usuario usuario, CategoriaEntrenamientoComunidad categoria);

    List<EntrenamientoUsuario> findByUsuarioAndNivel(Usuario usuario, NivelEntrenamiento nivel);

    List<EntrenamientoUsuario> findByUsuarioAndDuracionEnMinutosLessThanEqual(Usuario usuario,
            Integer duracionEnMinutos);

    List<EntrenamientoUsuario> findByUsuarioAndNombreContainingIgnoreCase(Usuario usuario, String nombre);

    boolean existsByUsuarioAndNombreIgnoreCaseAndCategoriaAndNivelAndDuracionEnMinutos(
            Usuario usuario,
            String nombre,
            CategoriaEntrenamientoComunidad categoria,
            NivelEntrenamiento nivel,
            Integer duracionEnMinutos);

    boolean existsByUsuarioAndNombreIgnoreCaseAndCategoriaAndNivelAndDuracionEnMinutosAndIdNot(
            Usuario usuario,
            String nombre,
            CategoriaEntrenamientoComunidad categoria,
            NivelEntrenamiento nivel,
            Integer duracionEnMinutos,
            Long id);
}