package com.example.digital_fit.service.Soporte;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.digital_fit.dto.Soporte.CrearSoporteDTO;
import com.example.digital_fit.dto.Soporte.SoporteDTO;
import com.example.digital_fit.dto.Soporte.Admin.CambiarEstadoSoporte;
import com.example.digital_fit.exception.OperacionNoPermitida;
import com.example.digital_fit.exception.RecursoNoEncontradoException;
import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.Enums.EstadoSoporte;
import com.example.digital_fit.model.Soporte.Soporte;
import com.example.digital_fit.repository.Auth.UsuarioRepository;
import com.example.digital_fit.repository.Soporte.SoporteRepository;

import jakarta.transaction.Transactional;

@Service
public class SoporteService {

    @Autowired
    private SoporteRepository soporteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final Logger log = LoggerFactory.getLogger(SoporteService.class);

    public List<SoporteDTO> listarTickets(String username) {

        log.info("Listando tickets del usuario {}", username);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        List<Soporte> tickets = soporteRepository.findByUsuarioOrderByFechaDesc(usuario);

        List<SoporteDTO> dtos = new ArrayList<>();

        for (Soporte t : tickets) {
            dtos.add(entityToDto(t));
        }

        return dtos;
    }

    public SoporteDTO verDetalles(Long id, String username) {

        log.debug("Buscando ticket con id: {}", id);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        Soporte ticket = soporteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket con id: " + id + " no encontrado."));

        if (!ticket.getUsuario().getId().equals(usuario.getId())) {
            throw new OperacionNoPermitida("No tienes permiso para ver este ticket.");
        }

        return entityToDto(ticket);
    }

    public SoporteDTO verDetallesAdmin(Long id) {

        log.debug("Admin consulta ticket con id: {}", id);

        Soporte ticket = soporteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket con id: " + id + " no encontrado."));

        return entityToDto(ticket);
    }

    @Transactional
    public void borrarTicket(Long id, String username) {

        log.info("Borrando ticket con id: {}", id);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        Soporte ticket = soporteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket con id: " + id + " no encontrado."));

        if (!ticket.getUsuario().getId().equals(usuario.getId())) {
            throw new OperacionNoPermitida("No tienes permiso para borrar este ticket.");
        }

        soporteRepository.delete(ticket);
        log.info("Ticket borrado con id: {}", id);
    }

    @Transactional
    public SoporteDTO crearTicket(CrearSoporteDTO dto, String username) {

        log.info("Creando ticket");

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        Soporte ticket = new Soporte();
        ticket.setAsunto(dto.getAsunto());
        ticket.setMensaje(dto.getMensaje());
        ticket.setUsuario(usuario);
        ticket.setFecha(LocalDateTime.now());
        ticket.setEstado(EstadoSoporte.ABIERTO);

        Soporte nuevoTicket = soporteRepository.save(ticket);

        log.info("Ticket creado con id: {}", nuevoTicket.getId());

        return entityToDto(nuevoTicket);
    }

    public List<SoporteDTO> listarTicketsAdmin() {

        log.info("Listando tickets para admin");

        List<Soporte> tickets = soporteRepository.findAllByOrderByFechaDesc();

        List<SoporteDTO> dtos = new ArrayList<>();

        for (Soporte t : tickets) {
            dtos.add(entityToDto(t));
        }

        return dtos;
    }

    @Transactional
    public SoporteDTO cambiarEstado(Long id, CambiarEstadoSoporte dto) {

        log.info("Cambiando estado de ticket con id: {}", id);

        Soporte ticket = soporteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket con id: " + id + " no encontrado."));

        ticket.setEstado(dto.getEstado());

        Soporte nuevoTicket = soporteRepository.save(ticket);

        return entityToDto(nuevoTicket);
    }

    @Transactional
    public void borrarTicketCerrado(Long id) {

        log.info("Borrando ticket con id: {}", id);

        Soporte ticket = soporteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket con id: " + id + " no encontrado."));

        if (!ticket.getEstado().equals(EstadoSoporte.CERRADO)) {
            throw new OperacionNoPermitida("No se puede borrar un ticket que no esté cerrado.");
        }

        soporteRepository.delete(ticket);

        log.info("Ticket borrado con id: {}", id);
    }

    public Soporte dtoToEntity(SoporteDTO dto) {
        Soporte soporte = new Soporte();
        soporte.setId(dto.getId());
        soporte.setAsunto(dto.getAsunto());
        soporte.setMensaje(dto.getMensaje());
        soporte.setFecha(dto.getFecha());
        soporte.setEstado(dto.getEstado());
        return soporte;
    }

    public SoporteDTO entityToDto(Soporte soporte) {
        SoporteDTO dto = new SoporteDTO();
        dto.setId(soporte.getId());
        dto.setAsunto(soporte.getAsunto());
        dto.setMensaje(soporte.getMensaje());
        dto.setFecha(soporte.getFecha());
        dto.setEstado(soporte.getEstado());
        return dto;
    }
}