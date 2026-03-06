package com.example.digital_fit.repository.Entrenamientos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.digital_fit.model.Entrenamientos.EntrenamientoComunidad;
import com.example.digital_fit.model.Enums.CategoriaEntrenamientoComunidad;
import com.example.digital_fit.model.Enums.NivelEntrenamiento;

@Repository
public interface EntrenamientoComunidadRepository extends JpaRepository<EntrenamientoComunidad, Long> {
    List<EntrenamientoComunidad> findByCategoria(CategoriaEntrenamientoComunidad categoria);

    List<EntrenamientoComunidad> findByNivel(NivelEntrenamiento nivel);

    List<EntrenamientoComunidad> findByCategoriaAndNivel(CategoriaEntrenamientoComunidad categoria,
            NivelEntrenamiento nivel);

    List<EntrenamientoComunidad> findAllByOrderByFechaPublicacionDesc();

    List<EntrenamientoComunidad> findAllByOrderByDuracionEnMinutosAsc();
}
