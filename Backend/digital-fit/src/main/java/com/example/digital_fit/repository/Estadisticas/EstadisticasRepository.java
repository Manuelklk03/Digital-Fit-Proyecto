package com.example.digital_fit.repository.Estadisticas;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.example.digital_fit.model.Auth.Usuario;

public interface EstadisticasRepository extends Repository<Object, Long> {
    @Query("""
            SELECT COUNT(h)
            FROM HistorialEntrenamientos h
            WHERE h.usuario = :usuario
            """)
    Long contarEntrenamientos(@Param("usuario") Usuario usuario);

    @Query("""
            SELECT SUM(h.duracionMinutos)
            FROM HistorialEntrenamientos h
            WHERE h.usuario = :usuario
            """)
    Integer sumarMinutosEntrenados(@Param("usuario") Usuario usuario);

    @Query("""
            SELECT COUNT(DISTINCT h.centroPrivado.id)
            FROM HistorialEntrenamientos h
            WHERE h.usuario = :usuario
            AND h.centroPrivado IS NOT NULL
            """)
    Long contarCentrosPrivadosVisitados(@Param("usuario") Usuario usuario);

    @Query("""
            SELECT COUNT(DISTINCT h.lugarPublico.id)
            FROM HistorialEntrenamientos h
            WHERE h.usuario = :usuario
            AND h.lugarPublico IS NOT NULL
            """)
    Long contarLugaresPublicosVisitados(@Param("usuario") Usuario usuario);

    @Query("""
            SELECT h.entrenamientoBase.nombre
            FROM HistorialEntrenamientos h
            WHERE h.usuario = :usuario
            AND h.entrenamientoBase IS NOT NULL
            GROUP BY h.entrenamientoBase.nombre
            ORDER BY COUNT(h) DESC
            LIMIT 1
            """)
    String entrenamientoMasRealizado(@Param("usuario") Usuario usuario);

    @Query("""
            SELECT AVG(h.duracionMinutos)
            FROM HistorialEntrenamientos h
            WHERE h.usuario = :usuario
            """)
    Double promedioDuracionEntrenamientos(@Param("usuario") Usuario usuario);

}
