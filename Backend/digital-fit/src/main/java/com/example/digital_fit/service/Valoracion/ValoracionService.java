package com.example.digital_fit.service.Valoracion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.digital_fit.dto.Valoracion.CrearValoracionDTO;
import com.example.digital_fit.dto.Valoracion.ValoracionDTO;
import com.example.digital_fit.exception.ErrorArgumentoException;
import com.example.digital_fit.exception.RecursoNoEncontradoException;
import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.Enums.TipoDeValoracion;
import com.example.digital_fit.model.Valoracion.Valoracion;
import com.example.digital_fit.repository.Auth.UsuarioRepository;
import com.example.digital_fit.repository.Valoracion.ValoracionRepository;

import jakarta.transaction.Transactional;

@Service
public class ValoracionService {

    @Autowired
    private ValoracionRepository valoracionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final Logger log = LoggerFactory.getLogger(ValoracionService.class);

    // Listar valoraciones de un contenido
    public List<ValoracionDTO> listarPorContenido(TipoDeValoracion tipo, Long contenidoId) {

        log.info("Listando valoraciones de un contenido");

        List<Valoracion> valoraciones = valoracionRepository.findByTipoDeValoracionAndIdRelacionado(tipo, contenidoId);

        List<ValoracionDTO> dtos = new ArrayList<>();

        for (Valoracion v : valoraciones) {
            dtos.add(entityToDto(v));
        }

        return dtos;
    }

    // Listar mis valoraciones
    public List<ValoracionDTO> listarMisValoraciones(String username) {

        log.info("Listando mis valoraciones");

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        List<Valoracion> valoraciones = valoracionRepository.findByUsuario(usuario);

        List<ValoracionDTO> dtos = new ArrayList<>();

        for (Valoracion v : valoraciones) {
            dtos.add(entityToDto(v));
        }

        return dtos;
    }

    // Borrar mi valoracion
    @Transactional
    public void borrarMiValoracion(TipoDeValoracion tipo, Long contenidoId, String username) {

        log.info("Borrando mi valoracion");

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        Valoracion valoracion = valoracionRepository
                .findByUsuarioAndTipoDeValoracionAndIdRelacionado(usuario, tipo, contenidoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Valoración no encontrada"));

        log.info("Valoracion con id {} borrada", valoracion.getId());

        valoracionRepository.delete(valoracion);

    }

    @Transactional
    public ValoracionDTO crearOActualizar(TipoDeValoracion tipo, Long contenidoId, CrearValoracionDTO dto,
            String username) {

        log.info("Creando o actualizando valoracion");

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        // Validar puntuacion
        if (dto.getPuntuacion() == null || dto.getPuntuacion() < 1 || dto.getPuntuacion() > 5) {
            throw new ErrorArgumentoException("La puntuación debe estar entre 1 y 5");
        }

        // Buscar si ya existe valoración del usuario para ese contenido
        Valoracion valoracion = valoracionRepository
                .findByUsuarioAndTipoDeValoracionAndIdRelacionado(usuario, tipo, contenidoId).orElseGet(() -> Valoracion
                        .builder().usuario(usuario).tipoDeValoracion(tipo).idRelacionado(contenidoId).build());

        // Actualizar
        valoracion.setPuntuacion(dto.getPuntuacion());
        valoracion.setComentario(dto.getComentario());

        // Fecha de creacxion o actualización
        valoracion.setFecha(LocalDateTime.now());

        Valoracion guardada = valoracionRepository.save(valoracion);

        log.info("Valoracion con id {} guardada/actualizada", guardada.getId());

        return entityToDto(guardada);
    }

    // Mappers
    private ValoracionDTO entityToDto(Valoracion valoracion) {
        ValoracionDTO dto = new ValoracionDTO();

        dto.setId(valoracion.getId());
        dto.setPuntuacion(valoracion.getPuntuacion());
        dto.setComentario(valoracion.getComentario());
        dto.setTipoDeValoracion(valoracion.getTipoDeValoracion());
        dto.setContenidoId(valoracion.getIdRelacionado());

        dto.setUsuarioId(valoracion.getUsuario().getId());
        dto.setUsername(valoracion.getUsuario().getUsername());

        dto.setFecha(valoracion.getFecha());
        return dto;
    }

    private Valoracion dtoToEntity(ValoracionDTO dto) {
        Valoracion valoracion = new Valoracion();
        valoracion.setId(dto.getId());
        valoracion.setPuntuacion(dto.getPuntuacion());
        valoracion.setComentario(dto.getComentario());
        valoracion.setTipoDeValoracion(dto.getTipoDeValoracion());
        valoracion.setIdRelacionado(dto.getContenidoId());

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        valoracion.setUsuario(usuario);

        valoracion.setFecha(dto.getFecha());

        return valoracion;
    }
}
