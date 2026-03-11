package com.example.digital_fit.service.Estadisticas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.digital_fit.dto.Estadisticas.EstadisticaUsuarioDTO;
import com.example.digital_fit.exception.RecursoNoEncontradoException;
import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.repository.Auth.UsuarioRepository;
import com.example.digital_fit.repository.Estadisticas.EstadisticasRepository;

@Service
public class EstadisticasService {

    @Autowired
    private EstadisticasRepository estadisticasRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final Logger log = LoggerFactory.getLogger(EstadisticasService.class);

    public EstadisticaUsuarioDTO obtenerEstadisticas(String username) {

        log.info("Obteniendo estadisticas del usuario {}", username);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado."));

        EstadisticaUsuarioDTO dto = new EstadisticaUsuarioDTO();

        Long entrenamientosRealizados = estadisticasRepository.contarEntrenamientos(usuario);

        Integer minutosEntrenados = estadisticasRepository.sumarMinutosEntrenados(usuario);

        Double promedioMinutosEntrenamiento = estadisticasRepository.promedioDuracionEntrenamientos(usuario);

        Long centrosPrivadosVisitados = estadisticasRepository.contarCentrosPrivadosVisitados(usuario);

        Long lugaresPublicosVisitados = estadisticasRepository.contarLugaresPublicosVisitados(usuario);

        String entrenamientoMasRealizado = estadisticasRepository.entrenamientoMasRealizado(usuario);

        dto.setEntrenamientosRealizados(entrenamientosRealizados != null ? entrenamientosRealizados : 0L);
        dto.setMinutosEntrenados(minutosEntrenados != null ? minutosEntrenados : 0);
        dto.setPromedioMinutosEntrenamiento(promedioMinutosEntrenamiento != null ? promedioMinutosEntrenamiento : 0.0);
        dto.setCentrosPrivadosVisitados(centrosPrivadosVisitados != null ? centrosPrivadosVisitados : 0L);
        dto.setLugaresPublicosVisitados(lugaresPublicosVisitados != null ? lugaresPublicosVisitados : 0L);
        dto.setEntrenamientoMasRealizado(entrenamientoMasRealizado != null ? entrenamientoMasRealizado : "N/A");

        log.info("Estadisticas del usuario {} obtenidas exitosamente", username);

        return dto;
    }
}
