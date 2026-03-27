package com.example.digital_fit.repository.Estadisticas;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.Entrenamientos.HistorialEntrenamientos;

public interface EstadisticasRepository extends Repository<HistorialEntrenamientos, Long> {

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
                        SELECT AVG(h.duracionMinutos)
                        FROM HistorialEntrenamientos h
                        WHERE h.usuario = :usuario
                        """)
        Double promedioDuracionEntrenamientos(@Param("usuario") Usuario usuario);

        @Query(value = """
                        SELECT
                            COALESCE(
                                (SELECT COUNT(DISTINCT h.centro_privado_base_id)
                                 FROM historial_entrenamientos h
                                 WHERE h.usuario_id = :usuarioId
                                 AND h.centro_privado_base_id IS NOT NULL), 0
                            )
                            +
                            COALESCE(
                                (SELECT COUNT(DISTINCT h.centro_privado_usuario_id)
                                 FROM historial_entrenamientos h
                                 WHERE h.usuario_id = :usuarioId
                                 AND h.centro_privado_usuario_id IS NOT NULL), 0
                            )
                        """, nativeQuery = true)
        Long contarCentrosPrivadosVisitados(@Param("usuarioId") Long usuarioId);

        @Query(value = """
                        SELECT
                            COALESCE(
                                (SELECT COUNT(DISTINCT h.lugar_publico_base_id)
                                 FROM historial_entrenamientos h
                                 WHERE h.usuario_id = :usuarioId
                                 AND h.lugar_publico_base_id IS NOT NULL), 0
                            )
                            +
                            COALESCE(
                                (SELECT COUNT(DISTINCT h.lugar_publico_usuario_id)
                                 FROM historial_entrenamientos h
                                 WHERE h.usuario_id = :usuarioId
                                 AND h.lugar_publico_usuario_id IS NOT NULL), 0
                            )
                        """, nativeQuery = true)
        Long contarLugaresPublicosVisitados(@Param("usuarioId") Long usuarioId);

        @Query(value = """
                        SELECT nombre_entrenamiento
                        FROM (
                            SELECT eb.nombre AS nombre_entrenamiento, COUNT(*) AS total
                            FROM historial_entrenamientos h
                            JOIN entrenamientos_base eb ON h.entrenamiento_base_id = eb.id
                            WHERE h.usuario_id = :usuarioId
                            GROUP BY eb.nombre

                            UNION ALL

                            SELECT eu.nombre AS nombre_entrenamiento, COUNT(*) AS total
                            FROM historial_entrenamientos h
                            JOIN entrenamientos_usuario eu ON h.entrenamiento_usuario_id = eu.id
                            WHERE h.usuario_id = :usuarioId
                            GROUP BY eu.nombre
                        ) t
                        ORDER BY total DESC, nombre_entrenamiento ASC
                        LIMIT 1
                        """, nativeQuery = true)
        String entrenamientoMasRealizado(@Param("usuarioId") Long usuarioId);
}