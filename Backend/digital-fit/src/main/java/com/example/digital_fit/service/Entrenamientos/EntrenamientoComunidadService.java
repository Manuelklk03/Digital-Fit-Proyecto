package com.example.digital_fit.service.Entrenamientos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.digital_fit.dto.Entrenamientos.EntrenamientoComunidadDTO;
import com.example.digital_fit.exception.RecursoNoEncontradoException;
import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.Entrenamientos.EntrenamientoComunidad;
import com.example.digital_fit.repository.Auth.UsuarioRepository;
import com.example.digital_fit.repository.Entrenamientos.EntrenamientoComunidadRepository;

import jakarta.transaction.Transactional;

@Service
public class EntrenamientoComunidadService {

    @Autowired
    private EntrenamientoComunidadRepository entrenamientoComunidadRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Listar entrenamientos de la comunidad
    public List<EntrenamientoComunidadDTO> listarTodos() {

        List<EntrenamientoComunidad> entrenamientos = entrenamientoComunidadRepository
                .findAllByOrderByFechaPublicacionDesc();

        List<EntrenamientoComunidadDTO> dtos = new ArrayList<>();

        for (EntrenamientoComunidad entrenamiento : entrenamientos) {
            dtos.add(entityToDto(entrenamiento));
        }

        return dtos;
    }

    // Crear entrenamiento de la comunidad
    @Transactional
    public EntrenamientoComunidadDTO crearEntrenamiento(EntrenamientoComunidadDTO dto, String username) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        EntrenamientoComunidad entrenamiento = new EntrenamientoComunidad();
        entrenamiento.setNombre(dto.getNombre());
        entrenamiento.setDescripcion(dto.getDescripcion());
        entrenamiento.setCategoria(dto.getCategoria());
        entrenamiento.setNivel(dto.getNivel());
        entrenamiento.setDuracionEnMinutos(dto.getDuracionEnMinutos());
        entrenamiento.setUsuario(usuario);
        entrenamiento.setFechaPublicacion(dto.getFechaPublicacion());

        EntrenamientoComunidad entrenamientoGuardado = entrenamientoComunidadRepository.save(entrenamiento);

        return entityToDto(entrenamientoGuardado);
    }

    // Mappers:
    private EntrenamientoComunidadDTO entityToDto(EntrenamientoComunidad entrenamiento) {
        EntrenamientoComunidadDTO dto = new EntrenamientoComunidadDTO();

        dto.setId(entrenamiento.getId());
        dto.setNombre(entrenamiento.getNombre());
        dto.setDescripcion(entrenamiento.getDescripcion());
        dto.setCategoria(entrenamiento.getCategoria());
        dto.setNivel(entrenamiento.getNivel());
        dto.setDuracionEnMinutos(entrenamiento.getDuracionEnMinutos());
        dto.setFechaPublicacion(entrenamiento.getFechaPublicacion());
        dto.setUsuario(entrenamiento.getUsuario().getUsername());

        return dto;
    }

    private EntrenamientoComunidad dtoToEntity(EntrenamientoComunidadDTO dto) {
        EntrenamientoComunidad entrenamiento = new EntrenamientoComunidad();

        entrenamiento.setNombre(dto.getNombre());
        entrenamiento.setDescripcion(dto.getDescripcion());
        entrenamiento.setCategoria(dto.getCategoria());
        entrenamiento.setNivel(dto.getNivel());
        entrenamiento.setDuracionEnMinutos(dto.getDuracionEnMinutos());

        // Asignar el usuario (asumiendo que el DTO tiene el username)
        if (dto.getUsuario() != null) {
            entrenamiento.setUsuario(usuarioRepository.findByUsername(dto.getUsuario()).orElse(null));
        }

        return entrenamiento;
    }
}
