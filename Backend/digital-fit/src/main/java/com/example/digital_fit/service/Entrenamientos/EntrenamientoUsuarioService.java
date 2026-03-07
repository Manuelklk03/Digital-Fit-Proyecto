package com.example.digital_fit.service.Entrenamientos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.digital_fit.dto.Entrenamientos.CrearEntrenamientoUsuario;
import com.example.digital_fit.dto.Entrenamientos.EntrenamientoUsuarioDTO;
import com.example.digital_fit.exception.OperacionNoPermitida;
import com.example.digital_fit.exception.RecursoNoEncontradoException;
import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.Entrenamientos.EntrenamientoBase;
import com.example.digital_fit.model.Entrenamientos.EntrenamientoComunidad;
import com.example.digital_fit.model.Entrenamientos.EntrenamientoUsuario;
import com.example.digital_fit.repository.Auth.UsuarioRepository;
import com.example.digital_fit.repository.Entrenamientos.EntrenamientoBaseRepository;
import com.example.digital_fit.repository.Entrenamientos.EntrenamientoComunidadRepository;
import com.example.digital_fit.repository.Entrenamientos.EntrenamientoUsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class EntrenamientoUsuarioService {

    @Autowired
    private EntrenamientoUsuarioRepository entrenamientoUsuarioRepository;

    @Autowired
    private EntrenamientoBaseRepository entrenamientoBaseRepository;

    @Autowired
    private EntrenamientoComunidadRepository entrenamientoComunidadRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<EntrenamientoUsuarioDTO> listarMisEntrenamientos(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        List<EntrenamientoUsuario> entrenamientos = entrenamientoUsuarioRepository.findByUsuario(usuario);

        List<EntrenamientoUsuarioDTO> dtos = new ArrayList<>();
        for (EntrenamientoUsuario entrenamiento : entrenamientos) {
            dtos.add(entityToDto(entrenamiento));
        }
        return dtos;
    }

    @Transactional
    public EntrenamientoUsuarioDTO crearEntrenamientoPersonalizado(CrearEntrenamientoUsuario dto, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        EntrenamientoUsuario entrenamientoUsuario = new EntrenamientoUsuario();
        entrenamientoUsuario.setNombre(dto.getNombre());
        entrenamientoUsuario.setDescripcion(dto.getDescripcion());
        entrenamientoUsuario.setCategoria(dto.getCategoria());
        entrenamientoUsuario.setNivel(dto.getNivel());
        entrenamientoUsuario.setDuracionEnMinutos(dto.getDuracionEnMinutos());
        entrenamientoUsuario.setUsuario(usuario);

        EntrenamientoUsuario entrenamientoUsuarioGuardado = entrenamientoUsuarioRepository.save(entrenamientoUsuario);
        return entityToDto(entrenamientoUsuarioGuardado);
    }

    // Para añadir entrenamientos de la app a el apartado mis entrenamientos:
    @Transactional
    public EntrenamientoUsuarioDTO añadirDesdeBase(Long idBase, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        EntrenamientoBase entrenamientoBase = entrenamientoBaseRepository.findById(idBase)
                .orElseThrow(() -> new RecursoNoEncontradoException("Entrenamiento base no encontrado"));

        EntrenamientoUsuario entrenamientoUsuario = new EntrenamientoUsuario();
        entrenamientoUsuario.setNombre(entrenamientoBase.getNombre());
        entrenamientoUsuario.setDescripcion(entrenamientoBase.getDescripcion());
        entrenamientoUsuario.setCategoria(entrenamientoBase.getCategoria());
        entrenamientoUsuario.setNivel(entrenamientoBase.getNivel());
        entrenamientoUsuario.setDuracionEnMinutos(entrenamientoBase.getDuracionEnMinutos());
        entrenamientoUsuario.setUsuario(usuario);

        EntrenamientoUsuario entrenamientoUsuarioGuardado = entrenamientoUsuarioRepository.save(entrenamientoUsuario);
        return entityToDto(entrenamientoUsuarioGuardado);
    }

    // Para añadir entrenamientos de la comunidad a el apartado mis entrenamientos:
    @Transactional
    public EntrenamientoUsuarioDTO añadirDesdeComunidad(Long idComunidad, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        EntrenamientoComunidad entrenamientoComunidad = entrenamientoComunidadRepository.findById(idComunidad)
                .orElseThrow(() -> new RecursoNoEncontradoException("Entrenamiento comunidad no encontrado"));

        EntrenamientoUsuario entrenamientoUsuario = new EntrenamientoUsuario();

        entrenamientoUsuario.setNombre(entrenamientoComunidad.getNombre());

        entrenamientoUsuario.setDescripcion(entrenamientoComunidad.getDescripcion());

        entrenamientoUsuario.setCategoria(entrenamientoComunidad.getCategoria());

        entrenamientoUsuario.setNivel(entrenamientoComunidad.getNivel());

        entrenamientoUsuario.setDuracionEnMinutos(entrenamientoComunidad.getDuracionEnMinutos());

        entrenamientoUsuario.setUsuario(usuario);

        EntrenamientoUsuario entrenamientoUsuarioGuardado = entrenamientoUsuarioRepository.save(entrenamientoUsuario);

        return entityToDto(entrenamientoUsuarioGuardado);
    }

    // Para borrar entrenamientos de mis entrenamientos:
    @Transactional
    public void borrarDeMisEntrenamientos(Long id, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        EntrenamientoUsuario entrenamientoUsuario = entrenamientoUsuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Entrenamiento usuario no encontrado"));

        if (!entrenamientoUsuario.getUsuario().getId().equals(usuario.getId())) {
            throw new OperacionNoPermitida("No tienes permiso para borrar este entrenamiento");
        }
        entrenamientoUsuarioRepository.delete(entrenamientoUsuario);
    }

    // Mappers:
    public EntrenamientoUsuarioDTO entityToDto(EntrenamientoUsuario entrenamientoUsuario) {
        EntrenamientoUsuarioDTO entrenamientoUsuarioDTO = new EntrenamientoUsuarioDTO();
        entrenamientoUsuarioDTO.setId(entrenamientoUsuario.getId());
        entrenamientoUsuarioDTO.setNombre(entrenamientoUsuario.getNombre());
        entrenamientoUsuarioDTO.setDescripcion(entrenamientoUsuario.getDescripcion());
        entrenamientoUsuarioDTO.setCategoria(entrenamientoUsuario.getCategoria());
        entrenamientoUsuarioDTO.setNivel(entrenamientoUsuario.getNivel());
        entrenamientoUsuarioDTO.setDuracionEnMinutos(entrenamientoUsuario.getDuracionEnMinutos());

        return entrenamientoUsuarioDTO;
    }

    public EntrenamientoUsuario dtoToEntity(EntrenamientoUsuarioDTO dto) {
        EntrenamientoUsuario entrenamientoUsuario = new EntrenamientoUsuario();
        entrenamientoUsuario.setId(dto.getId());
        entrenamientoUsuario.setNombre(dto.getNombre());
        entrenamientoUsuario.setDescripcion(dto.getDescripcion());
        entrenamientoUsuario.setCategoria(dto.getCategoria());
        entrenamientoUsuario.setNivel(dto.getNivel());
        entrenamientoUsuario.setDuracionEnMinutos(dto.getDuracionEnMinutos());

        return entrenamientoUsuario;
    }
}
