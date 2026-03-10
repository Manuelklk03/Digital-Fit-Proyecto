package com.example.digital_fit.service.Estadisticas;

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

    public EstadisticaUsuarioDTO obtenerEstadisticas(String username) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado."));

        EstadisticaUsuarioDTO dto = new EstadisticaUsuarioDTO();

        dto.setEntrenamientosRealizados(estadisticasRepository.contarEntrenamientos(usuario));

        dto.setMinutosEntrenados(estadisticasRepository.sumarMinutosEntrenados(usuario));

        dto.setPromedioMinutosEntrenamiento(estadisticasRepository.promedioDuracionEntrenamientos(usuario));

        dto.setCentrosPrivadosVisitados(estadisticasRepository.contarCentrosPrivadosVisitados(usuario));

        dto.setLugaresPublicosVisitados(estadisticasRepository.contarLugaresPublicosVisitados(usuario));

        dto.setEntrenamientoMasRealizado(estadisticasRepository.entrenamientoMasRealizado(usuario));

        return dto;
    }
}
