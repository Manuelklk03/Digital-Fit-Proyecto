package com.example.digital_fit.controller.Soporte.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.digital_fit.dto.Soporte.SoporteDTO;
import com.example.digital_fit.dto.Soporte.Admin.CambiarEstadoSoporte;
import com.example.digital_fit.dto.Soporte.CrearMensajeSoporteDTO;
import com.example.digital_fit.dto.Soporte.MensajeSoporteDTO;
import com.example.digital_fit.service.Soporte.SoporteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/soporte")
public class SoporteAdminRestController {

    @Autowired
    private SoporteService soporteService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SoporteDTO>> verTickets() {
        return ResponseEntity.status(HttpStatus.OK).body(soporteService.listarTicketsAdmin());
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SoporteDTO> cambiarEstado(@PathVariable Long id, @RequestBody CambiarEstadoSoporte dto) {
        return ResponseEntity.status(HttpStatus.OK).body(soporteService.cambiarEstado(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> borrarTicket(@PathVariable Long id) {
        soporteService.borrarTicketCerrado(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SoporteDTO> verDetalles(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(soporteService.verDetallesAdmin(id));
    }

    @GetMapping("/{id}/mensajes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MensajeSoporteDTO>> verMensajes(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(soporteService.listarMensajesTicketAdmin(id));
    }

    @PostMapping("/{id}/mensajes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MensajeSoporteDTO> enviarMensaje(
            @PathVariable Long id,
            @Valid @RequestBody CrearMensajeSoporteDTO dto,
            Authentication authentication) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(soporteService.enviarMensajeAdmin(id, dto, authentication.getName()));
    }
}