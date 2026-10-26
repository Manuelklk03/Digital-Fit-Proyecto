package com.example.digital_fit.service.Soporte;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // Listar tickets:
    public List<SoporteDTO> listarTickets(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        List<Soporte> tickets = soporteRepository.findByUsuarioOrderByFechaDesc(usuario);

        List<SoporteDTO> dtos = new ArrayList<>();

        for (Soporte t : tickets) {
            dtos.add(entityToDto(t));
        }
        return dtos;

    }

    // Ver detalles de un ticket:
    public SoporteDTO verDetalles(Long id, String username) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        Soporte ticket = soporteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket con id: " + id + " No encontrado."));

        if (!ticket.getUsuario().getId().equals(usuario.getId())) {
            throw new OperacionNoPermitida("No tienes permiso para ver este ticket.");
        }

        return entityToDto(ticket);
    }

    // Borrar un ticket:
    @Transactional
    public void borrarTicket(Long id, String username) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        Soporte ticket = soporteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket con id: " + id + " No encontrado."));

        if (!ticket.getUsuario().getId().equals(usuario.getId())) {
            throw new OperacionNoPermitida("No tienes permiso para borrar este ticket.");
        }

        soporteRepository.delete(ticket);
    }

    // Crear Ticket:
    @Transactional
    public SoporteDTO crearTicket(CrearSoporteDTO dto, String username) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        Soporte ticket = new Soporte();

        ticket.setAsunto(dto.getAsunto());
        ticket.setMensaje(dto.getMensaje());

        // Datos automaticos:
        ticket.setUsuario(usuario);
        ticket.setFecha(LocalDateTime.now());
        ticket.setEstado(EstadoSoporte.ABIERTO);

        Soporte nuevoTicket = soporteRepository.save(ticket);

        return entityToDto(nuevoTicket);

    }

    // Metodo para panel de admin:

    // Listar todos los tickets:
    public List<SoporteDTO> listarTicketsAdmin() {
        List<Soporte> tickets = soporteRepository.findAllByOrderByFechaDesc();
        List<SoporteDTO> dtos = new ArrayList<>();
        for (Soporte t : tickets) {
            dtos.add(entityToDto(t));
        }
        return dtos;
    }

    // Cambiar estado de un ticket:
    @Transactional
    public SoporteDTO cambiarEstado(Long id, CambiarEstadoSoporte dto) {

        Soporte ticket = soporteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket con id: " + id + " No encontrado."));

        ticket.setEstado(dto.getEstado());

        Soporte nuevoTicket = soporteRepository.save(ticket);

        return entityToDto(nuevoTicket);
    }

    // Eliminar un ticket en estado cerrado:
    @Transactional
    public void borrarTicketCerrado(Long id) {
        Soporte ticket = soporteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket con id: " + id + " No encontrado."));

        if (!ticket.getEstado().equals(EstadoSoporte.CERRADO)) {
            throw new OperacionNoPermitida("No se puede borrar un ticket que no este cerrado.");
        }

        soporteRepository.delete(ticket);
    }

    // Mappers:
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
