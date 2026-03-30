package com.example.digital_fit.repository.Entrenamientos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.digital_fit.model.Entrenamientos.EntrenamientoBase;
import com.example.digital_fit.model.Enums.CategoriaEntrenamientoComunidad;
import com.example.digital_fit.model.Enums.NivelEntrenamiento;

@Repository
public interface EntrenamientoBaseRepository extends JpaRepository<EntrenamientoBase, Long> {

    List<EntrenamientoBase> findByCategoria(CategoriaEntrenamientoComunidad categoria);

    List<EntrenamientoBase> findByNivel(NivelEntrenamiento nivel);

    List<EntrenamientoBase> findByDuracionEnMinutosLessThanEqual(Integer duracionEnMinutos);

    List<EntrenamientoBase> findByNombreContainingIgnoreCase(String nombre);
}