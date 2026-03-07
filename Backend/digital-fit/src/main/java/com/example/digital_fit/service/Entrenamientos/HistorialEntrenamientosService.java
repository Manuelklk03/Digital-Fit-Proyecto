package com.example.digital_fit.service.Entrenamientos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.digital_fit.dto.Entrenamientos.HistorialEntrenamientosDTO;
import com.example.digital_fit.exception.OperacionNoPermitida;
import com.example.digital_fit.exception.RecursoNoEncontradoException;
import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.Entrenamientos.HistorialEntrenamientos;
import com.example.digital_fit.repository.Auth.UsuarioRepository;
import com.example.digital_fit.repository.CentroPrivado.CentroPrivadoUsuarioRepository;
import com.example.digital_fit.repository.Entrenamientos.EntrenamientoBaseRepository;
import com.example.digital_fit.repository.Entrenamientos.EntrenamientoUsuarioRepository;
import com.example.digital_fit.repository.Entrenamientos.HistorialEntrenamientosRepository;
import com.example.digital_fit.repository.LugarPublico.LugarPublicoUsuarioRepository;

@Service
public class HistorialEntrenamientosService {

    @Autowired
    private HistorialEntrenamientosRepository historialEntrenamientosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EntrenamientoBaseRepository entrenamientoBaseRepository;

    @Autowired
    private EntrenamientoUsuarioRepository entrenamientoUsuarioRepository;

    @Autowired
    private LugarPublicoUsuarioRepository lugarPublicoUsuarioRepository;

    @Autowired
    private CentroPrivadoUsuarioRepository centroPrivadoUsuarioRepository;

    // Listar historial del usuario;
    public List<HistorialEntrenamientosDTO> listarHistorialEntrenamientos(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        List<HistorialEntrenamientos> historial = historialEntrenamientosRepository
                .findByUsuarioOrderByFechaHoraDesc(usuario);

        List<HistorialEntrenamientosDTO> dtos = new ArrayList<>();

        for (HistorialEntrenamientos h : historial) {
            dtos.add(entityToDto(h));
        }
        return dtos;
    }

    // Ver detalles de un registro
    public HistorialEntrenamientosDTO verDetalles(Long id, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        HistorialEntrenamientos historial = historialEntrenamientosRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Registro con id: " + id + " No encontrado."));

        if (!historial.getUsuario().getId().equals(usuario.getId())) {
            throw new OperacionNoPermitida("No tienes permiso para ver este registro.");
        }
        return entityToDto(historial);
    }

    // Crear registro de entrenamiento realizado

    // Borrar registro

    // Mappers

    public HistorialEntrenamientosDTO entityToDto(HistorialEntrenamientos historial) {
        HistorialEntrenamientosDTO dto = new HistorialEntrenamientosDTO();

        dto.setId(historial.getId());

        if (historial.getEntrenamientoBase() != null) {
            dto.setEntrenamiento(historial.getEntrenamientoBase().getNombre());
        } else if (historial.getEntrenamientoUsuario() != null) {
            dto.setEntrenamiento(historial.getEntrenamientoUsuario().getNombre());
        }

        if (historial.getLugarPublico() != null) {
            dto.setLugar(historial.getLugarPublico().getNombre());
        } else if (historial.getCentroPrivado() != null) {
            dto.setLugar(historial.getCentroPrivado().getNombre());
        }

        dto.setFecha(historial.getFechaHora());
        dto.setDuracionEnMinutos(historial.getDuracionMinutos());
        dto.setNotas(historial.getNotas());

        return dto;
    }

    public HistorialEntrenamientos dtoToEntity(HistorialEntrenamientosDTO dto) {

        HistorialEntrenamientos historial = new HistorialEntrenamientos();

        historial.setId(dto.getId());
        historial.setFechaHora(dto.getFecha());
        historial.setDuracionMinutos(dto.getDuracionEnMinutos());
        historial.setNotas(dto.getNotas());

        // Las relaciones (usuario, entrenamiento, lugar, centro)
        // se asignan en el service porque necesitan buscarse en los repositories

        return historial;
    }

}
