package com.example.digital_fit.repository.Entrenamientos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.digital_fit.model.Entrenamientos.EntrenamientoUsuario;
import com.example.digital_fit.model.Enums.CategoriaEntrenamientoComunidad;
import com.example.digital_fit.model.Enums.NivelEntrenamiento;
import com.example.digital_fit.model.Auth.Usuario;

@Repository
public interface EntrenamientoUsuarioRepository extends JpaRepository<EntrenamientoUsuario, Long> {

    List<EntrenamientoUsuario> findByUsuarioId(Long usuarioId);

    @Query("""
            SELECT e FROM EntrenamientoUsuario e
            WHERE e.usuario = :usuario
            AND (e.activo IS NULL OR e.activo = true)
            """)
    List<EntrenamientoUsuario> findVisiblesByUsuario(@Param("usuario") Usuario usuario);

    @Query("""
            SELECT e FROM EntrenamientoUsuario e
            WHERE e.usuario = :usuario
            AND e.categoria = :categoria
            AND (e.activo IS NULL OR e.activo = true)
            """)
    List<EntrenamientoUsuario> findVisiblesByUsuarioAndCategoria(
            @Param("usuario") Usuario usuario,
            @Param("categoria") CategoriaEntrenamientoComunidad categoria);

    @Query("""
            SELECT e FROM EntrenamientoUsuario e
            WHERE e.usuario = :usuario
            AND e.nivel = :nivel
            AND (e.activo IS NULL OR e.activo = true)
            """)
    List<EntrenamientoUsuario> findVisiblesByUsuarioAndNivel(
            @Param("usuario") Usuario usuario,
            @Param("nivel") NivelEntrenamiento nivel);

    @Query("""
            SELECT e FROM EntrenamientoUsuario e
            WHERE e.usuario = :usuario
            AND e.duracionEnMinutos <= :duracionEnMinutos
            AND (e.activo IS NULL OR e.activo = true)
            """)
    List<EntrenamientoUsuario> findVisiblesByUsuarioAndDuracionEnMinutosLessThanEqual(
            @Param("usuario") Usuario usuario,
            @Param("duracionEnMinutos") Integer duracionEnMinutos);

    @Query("""
            SELECT e FROM EntrenamientoUsuario e
            WHERE e.usuario = :usuario
            AND LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))
            AND (e.activo IS NULL OR e.activo = true)
            """)
    List<EntrenamientoUsuario> findVisiblesByUsuarioAndNombreContainingIgnoreCase(
            @Param("usuario") Usuario usuario,
            @Param("nombre") String nombre);

    @Query("""
            SELECT e FROM EntrenamientoUsuario e
            WHERE e.usuario = :usuario
            AND LOWER(e.nombre) = LOWER(:nombre)
            AND (e.activo IS NULL OR e.activo = true)
            """)
    Optional<EntrenamientoUsuario> findVisibleByUsuarioAndNombreIgnoreCase(
            @Param("usuario") Usuario usuario,
            @Param("nombre") String nombre);
}
