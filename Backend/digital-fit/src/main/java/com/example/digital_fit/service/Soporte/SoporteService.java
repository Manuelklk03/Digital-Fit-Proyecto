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
import com.example.digital_fit.dto.Soporte.CrearMensajeSoporteDTO;
import com.example.digital_fit.dto.Soporte.MensajeSoporteDTO;
import com.example.digital_fit.exception.OperacionNoPermitida;
import com.example.digital_fit.exception.RecursoNoEncontradoException;
import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.Enums.EstadoSoporte;
import com.example.digital_fit.model.Enums.Rol;
import com.example.digital_fit.model.Soporte.MensajeSoporte;
import com.example.digital_fit.model.Soporte.Soporte;
import com.example.digital_fit.repository.Auth.UsuarioRepository;
import com.example.digital_fit.repository.Soporte.MensajeSoporteRepository;
import com.example.digital_fit.repository.Soporte.SoporteRepository;

import jakarta.transaction.Transactional;

@Service
public class SoporteService {

    @Autowired
    private SoporteRepository soporteRepository;

    @Autowired
    private MensajeSoporteRepository mensajeSoporteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final Logger log = LoggerFactory.getLogger(SoporteService.class);

    public List<SoporteDTO> listarTickets(String username) {
        log.info("Listando tickets del usuario {}", username);

        Usuario usuario = obtenerUsuario(username);

        List<Soporte> tickets = soporteRepository.findByUsuarioOrderByFechaDesc(usuario);
        List<SoporteDTO> dtos = new ArrayList<>();

        for (Soporte t : tickets) {
            dtos.add(entityToDto(t));
        }

        return dtos;
    }

    public SoporteDTO verDetalles(Long id, String username) {
        log.debug("Buscando ticket con id: {}", id);

        Usuario usuario = obtenerUsuario(username);
        Soporte ticket = obtenerTicketDeUsuario(id, usuario);

        return entityToDto(ticket);
    }

    public SoporteDTO verDetallesAdmin(Long id) {
        log.debug("Admin consulta ticket con id: {}", id);

        Soporte ticket = obtenerTicketAdmin(id);
        return entityToDto(ticket);
    }

    @Transactional
    public void borrarTicket(Long id, String username) {
        log.info("Borrando ticket con id: {}", id);

        Usuario usuario = obtenerUsuario(username);
        Soporte ticket = obtenerTicketDeUsuario(id, usuario);

        soporteRepository.delete(ticket);
        log.info("Ticket borrado con id: {}", id);
    }

    @Transactional
    public SoporteDTO crearTicket(CrearSoporteDTO dto, String username) {
        log.info("Creando ticket");

        Usuario usuario = obtenerUsuario(username);

        LocalDateTime ahora = LocalDateTime.now();

        Soporte ticket = new Soporte();
        ticket.setAsunto(dto.getAsunto());
        ticket.setMensaje(dto.getMensaje());
        ticket.setUsuario(usuario);
        ticket.setFecha(ahora);
        ticket.setEstado(EstadoSoporte.ABIERTO);

        Soporte nuevoTicket = soporteRepository.save(ticket);

        MensajeSoporte mensajeInicial = MensajeSoporte.builder()
                .ticket(nuevoTicket)
                .emisor(usuario)
                .contenido(dto.getMensaje())
                .fecha(ahora)
                .build();

        mensajeSoporteRepository.save(mensajeInicial);

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

        Soporte ticket = obtenerTicketAdmin(id);
        ticket.setEstado(dto.getEstado());

        Soporte nuevoTicket = soporteRepository.save(ticket);

        return entityToDto(nuevoTicket);
    }

    @Transactional
    public void borrarTicketCerrado(Long id) {
        log.info("Borrando ticket con id: {}", id);

        Soporte ticket = obtenerTicketAdmin(id);

        if (!ticket.getEstado().equals(EstadoSoporte.CERRADO)) {
            throw new OperacionNoPermitida("No se puede borrar un ticket que no esté cerrado.");
        }

        soporteRepository.delete(ticket);

        log.info("Ticket borrado con id: {}", id);
    }

    public List<MensajeSoporteDTO> listarMensajesTicketUsuario(Long id, String username) {
        Usuario usuario = obtenerUsuario(username);
        Soporte ticket = obtenerTicketDeUsuario(id, usuario);

        List<MensajeSoporte> mensajes = mensajeSoporteRepository.findByTicketOrderByFechaAsc(ticket);
        List<MensajeSoporteDTO> dtos = new ArrayList<>();

        for (MensajeSoporte mensaje : mensajes) {
            dtos.add(mensajeEntityToDto(mensaje));
        }

        return dtos;
    }

    public List<MensajeSoporteDTO> listarMensajesTicketAdmin(Long id) {
        Soporte ticket = obtenerTicketAdmin(id);

        List<MensajeSoporte> mensajes = mensajeSoporteRepository.findByTicketOrderByFechaAsc(ticket);
        List<MensajeSoporteDTO> dtos = new ArrayList<>();

        for (MensajeSoporte mensaje : mensajes) {
            dtos.add(mensajeEntityToDto(mensaje));
        }

        return dtos;
    }

    @Transactional
    public MensajeSoporteDTO enviarMensajeUsuario(Long id, CrearMensajeSoporteDTO dto, String username) {
        Usuario usuario = obtenerUsuario(username);
        Soporte ticket = obtenerTicketDeUsuario(id, usuario);

        if (ticket.getEstado() == EstadoSoporte.CERRADO) {
            throw new OperacionNoPermitida("No puedes responder a un ticket cerrado.");
        }

        boolean adminYaRespondio = mensajeSoporteRepository.existsByTicketAndEmisor_Rol(ticket, Rol.ADMIN);

        if (!adminYaRespondio) {
            throw new OperacionNoPermitida(
                    "Todavía no puedes responder porque ningún administrador ha iniciado la conversación.");
        }

        MensajeSoporte mensaje = MensajeSoporte.builder()
                .ticket(ticket)
                .emisor(usuario)
                .contenido(dto.getContenido())
                .fecha(LocalDateTime.now())
                .build();

        MensajeSoporte guardado = mensajeSoporteRepository.save(mensaje);

        return mensajeEntityToDto(guardado);
    }

    @Transactional
    public MensajeSoporteDTO enviarMensajeAdmin(Long id, CrearMensajeSoporteDTO dto, String username) {
        Usuario admin = obtenerUsuario(username);

        if (admin.getRol() != Rol.ADMIN) {
            throw new OperacionNoPermitida("No tienes permisos de administrador.");
        }

        Soporte ticket = obtenerTicketAdmin(id);

        if (ticket.getEstado() == EstadoSoporte.CERRADO) {
            throw new OperacionNoPermitida("No se puede responder a un ticket cerrado.");
        }

        if (ticket.getEstado() == EstadoSoporte.ABIERTO) {
            ticket.setEstado(EstadoSoporte.EN_PROCESO);
            soporteRepository.save(ticket);
        }

        MensajeSoporte mensaje = MensajeSoporte.builder()
                .ticket(ticket)
                .emisor(admin)
                .contenido(dto.getContenido())
                .fecha(LocalDateTime.now())
                .build();

        MensajeSoporte guardado = mensajeSoporteRepository.save(mensaje);

        return mensajeEntityToDto(guardado);
    }

    private Usuario obtenerUsuario(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
    }

    private Soporte obtenerTicketDeUsuario(Long id, Usuario usuario) {
        Soporte ticket = soporteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket con id: " + id + " no encontrado."));

        if (!ticket.getUsuario().getId().equals(usuario.getId())) {
            throw new OperacionNoPermitida("No tienes permiso para acceder a este ticket.");
        }

        return ticket;
    }

    private Soporte obtenerTicketAdmin(Long id) {
        return soporteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket con id: " + id + " no encontrado."));
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
        dto.setUsuarioId(soporte.getUsuario().getId());
        dto.setUsername(soporte.getUsuario().getUsername());
        return dto;
    }

    public MensajeSoporteDTO mensajeEntityToDto(MensajeSoporte mensaje) {
        MensajeSoporteDTO dto = new MensajeSoporteDTO();
        dto.setId(mensaje.getId());
        dto.setContenido(mensaje.getContenido());
        dto.setFecha(mensaje.getFecha());
        dto.setEmisorId(mensaje.getEmisor().getId());
        dto.setEmisorUsername(mensaje.getEmisor().getUsername());
        dto.setEmisorRol(mensaje.getEmisor().getRol());
        return dto;
    }
}