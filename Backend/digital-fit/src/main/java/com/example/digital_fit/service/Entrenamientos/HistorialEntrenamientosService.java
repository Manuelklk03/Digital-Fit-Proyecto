package com.example.digital_fit.service.Entrenamientos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.digital_fit.dto.Entrenamientos.CrearEntrenamientoHistorialDTO;
import com.example.digital_fit.dto.Entrenamientos.HistorialEntrenamientosDTO;
import com.example.digital_fit.exception.OperacionNoPermitida;
import com.example.digital_fit.exception.RecursoNoEncontradoException;
import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.CentroPrivado.CentroPrivadoUsuario;
import com.example.digital_fit.model.Entrenamientos.EntrenamientoBase;
import com.example.digital_fit.model.Entrenamientos.EntrenamientoUsuario;
import com.example.digital_fit.model.Entrenamientos.HistorialEntrenamientos;
import com.example.digital_fit.model.LugarPublico.LugarPublicoUsuario;
import com.example.digital_fit.repository.Auth.UsuarioRepository;
import com.example.digital_fit.repository.CentroPrivado.CentroPrivadoUsuarioRepository;
import com.example.digital_fit.repository.Entrenamientos.EntrenamientoBaseRepository;
import com.example.digital_fit.repository.Entrenamientos.EntrenamientoUsuarioRepository;
import com.example.digital_fit.repository.Entrenamientos.HistorialEntrenamientosRepository;
import com.example.digital_fit.repository.LugarPublico.LugarPublicoUsuarioRepository;

import jakarta.transaction.Transactional;

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

    private static final Logger log = LoggerFactory.getLogger(HistorialEntrenamientosService.class);

    // Listar filtrado;
    public List<HistorialEntrenamientosDTO> listarOFiltrarHistorial(String username, Integer duracionEnMinutos,
            LocalDateTime fecha) {

        log.info("Listar historial de entrenamientos");

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        List<HistorialEntrenamientos> registros = new ArrayList<>();

        if (duracionEnMinutos != null) {
            registros = historialEntrenamientosRepository
                    .findByUsuarioAndDuracionMinutosLessThanEqualOrderByFechaHoraDesc(usuario,
                            duracionEnMinutos);
        } else if (fecha != null) {
            registros = historialEntrenamientosRepository
                    .findByUsuarioAndFechaHoraAfterOrderByFechaHoraDesc(usuario, fecha);
        } else {
            registros = historialEntrenamientosRepository.findByUsuarioOrderByFechaHoraDesc(usuario);
        }

        List<HistorialEntrenamientosDTO> registrosDTO = new ArrayList<>();

        for (HistorialEntrenamientos r : registros) {
            registrosDTO.add(entityToDto(r));
        }

        return registrosDTO;
    }

    // Ver detalles de un registro
    public HistorialEntrenamientosDTO verDetalles(Long id, String username) {

        log.debug("Buscando registro con id: {}", id);

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
    @Transactional
    public HistorialEntrenamientosDTO crearHistorialEntrenamiento(CrearEntrenamientoHistorialDTO dto, String username) {

        log.info("Usuario {} crea registro de entrenamiento realizado", username);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        HistorialEntrenamientos historial = new HistorialEntrenamientos();

        historial.setUsuario(usuario);
        historial.setFechaHora(dto.getFecha());
        historial.setDuracionMinutos(dto.getDuracionEnMinutos());
        historial.setNotas(dto.getNotas());

        // Entrenamiento base
        if (dto.getEntrenamientoBaseId() != null) {
            EntrenamientoBase entrenamientoBase = entrenamientoBaseRepository.findById(dto.getEntrenamientoBaseId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Entrenamiento base con id: "
                            + dto.getEntrenamientoBaseId() + " No encontrado."));

            historial.setEntrenamientoBase(entrenamientoBase);
        }

        // Entrenamiento Usuario:
        if (dto.getEntrenamientoUsuarioId() != null) {
            EntrenamientoUsuario entrenamientoUsuario = entrenamientoUsuarioRepository
                    .findById(dto.getEntrenamientoUsuarioId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Entrenamiento usuario con id: "
                            + dto.getEntrenamientoUsuarioId() + " No encontrado."));

            historial.setEntrenamientoUsuario(entrenamientoUsuario);
        }

        // Lugar publico
        if (dto.getLugarPublicoId() != null) {
            LugarPublicoUsuario lugarPublico = lugarPublicoUsuarioRepository.findById(dto.getLugarPublicoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Lugar publico con id: "
                            + dto.getLugarPublicoId() + " No encontrado."));

            historial.setLugarPublico(lugarPublico);
        }

        // Centro privado
        if (dto.getCentroPrivadoId() != null) {

            CentroPrivadoUsuario centroPrivado = centroPrivadoUsuarioRepository.findById(dto.getCentroPrivadoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Centro privado con id: "
                            + dto.getCentroPrivadoId() + " No encontrado."));

            historial.setCentroPrivado(centroPrivado);
        }

        HistorialEntrenamientos historialGuardado = historialEntrenamientosRepository.save(historial);

        log.info("Historial guardado con id: {}", historialGuardado.getId());

        return entityToDto(historialGuardado);

    }

    // Borrar registro
    @Transactional
    public void borrarHistorialEntrenamiento(Long id, String username) {

        log.info("Usuario {} borra registro con id: {}", username, id);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        HistorialEntrenamientos historial = historialEntrenamientosRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Registro con id: " + id + " No encontrado."));

        if (!historial.getUsuario().getId().equals(usuario.getId())) {
            throw new OperacionNoPermitida("No tienes permiso para borrar este registro.");
        }

        log.info("Borrando registro con id: {}", id);

        historialEntrenamientosRepository.delete(historial);
    }

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
