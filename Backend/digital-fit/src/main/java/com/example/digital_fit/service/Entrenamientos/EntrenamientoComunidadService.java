package com.example.digital_fit.service.Entrenamientos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.digital_fit.dto.Entrenamientos.CrearEntrenamientoComunidadDTO;
import com.example.digital_fit.dto.Entrenamientos.EntrenamientoComunidadDTO;
import com.example.digital_fit.exception.RecursoNoEncontradoException;
import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.Entrenamientos.EntrenamientoComunidad;
import com.example.digital_fit.model.Enums.CategoriaEntrenamientoComunidad;
import com.example.digital_fit.model.Enums.NivelEntrenamiento;
import com.example.digital_fit.repository.Auth.UsuarioRepository;
import com.example.digital_fit.repository.Entrenamientos.EntrenamientoComunidadRepository;

import jakarta.transaction.Transactional;

@Service
public class EntrenamientoComunidadService {

    @Autowired
    private EntrenamientoComunidadRepository entrenamientoComunidadRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final Logger log = LoggerFactory.getLogger(EntrenamientoComunidadService.class);

    // Listar o filtrar entrenamientos de la comunidad
    public List<EntrenamientoComunidadDTO> listarOFiltrar(CategoriaEntrenamientoComunidad categoria,
            NivelEntrenamiento nivel, Integer duracionEnMinutos, String nombre) {

        log.debug("Listando entrenamientos comunidad");

        List<EntrenamientoComunidad> entrenamientosComunidad = new ArrayList<>();

        if (categoria != null) {

            entrenamientosComunidad = entrenamientoComunidadRepository.findByCategoria(categoria);

        } else if (nivel != null) {

            entrenamientosComunidad = entrenamientoComunidadRepository.findByNivel(nivel);

        } else if (duracionEnMinutos != null) {

            entrenamientosComunidad = entrenamientoComunidadRepository
                    .findByDuracionEnMinutosLessThanEqual(duracionEnMinutos);

        } else if (nombre != null) {

            entrenamientosComunidad = entrenamientoComunidadRepository.findByNombreContainingIgnoreCase(nombre);

        } else {

            entrenamientosComunidad = entrenamientoComunidadRepository.findAllByOrderByFechaPublicacionDesc();
        }

        List<EntrenamientoComunidadDTO> entrenamientosComunidadDTO = new ArrayList<>();

        for (EntrenamientoComunidad entrenamientoComunidad : entrenamientosComunidad) {
            entrenamientosComunidadDTO.add(entityToDto(entrenamientoComunidad));
        }

        return entrenamientosComunidadDTO;

    }

    // VER DETALLES:
    public EntrenamientoComunidadDTO verDetalles(Long id) {

        log.debug("Buscando entrenamiento con id: {}", id);

        EntrenamientoComunidad entrenamiento = entrenamientoComunidadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Entrenamiento no encontrado"));

        return entityToDto(entrenamiento);
    }

    // Crear entrenamiento de la comunidad
    @Transactional
    public EntrenamientoComunidadDTO crearEntrenamiento(CrearEntrenamientoComunidadDTO dto, String username) {

        log.info("Usuario {} crea entrenamiento de la comunidad", username);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        EntrenamientoComunidad entrenamiento = new EntrenamientoComunidad();

        entrenamiento.setNombre(dto.getNombre());
        entrenamiento.setDescripcion(dto.getDescripcion());
        entrenamiento.setCategoria(dto.getCategoria());
        entrenamiento.setNivel(dto.getNivel());
        entrenamiento.setDuracionEnMinutos(dto.getDuracionEnMinutos());
        entrenamiento.setUsuario(usuario);
        entrenamiento.setFechaPublicacion(LocalDateTime.now());

        EntrenamientoComunidad entrenamientoGuardado = entrenamientoComunidadRepository.save(entrenamiento);

        log.info("Entrenamiento {} creado para usuario {}", dto.getNombre(), username);
        
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
