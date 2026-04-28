package com.example.digital_fit.controller.Soporte;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.digital_fit.dto.Soporte.CrearSoporteDTO;
import com.example.digital_fit.dto.Soporte.SoporteDTO;
import com.example.digital_fit.dto.Soporte.CrearMensajeSoporteDTO;
import com.example.digital_fit.dto.Soporte.MensajeSoporteDTO;
import com.example.digital_fit.service.Soporte.SoporteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/soporte")
public class SoporteRestController {

    @Autowired
    private SoporteService soporteService;

    @PostMapping
    public ResponseEntity<SoporteDTO> crearTicket(@Valid @RequestBody CrearSoporteDTO dto,
            Authentication authentication) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(soporteService.crearTicket(dto, authentication.getName()));
    }

    @GetMapping("/mis-tickets")
    public ResponseEntity<List<SoporteDTO>> verMisTickets(Authentication authentication) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(soporteService.listarTickets(authentication.getName()));
    }

    @GetMapping("/mis-tickets/{id}")
    public ResponseEntity<SoporteDTO> verDetalles(@PathVariable Long id, Authentication authentication) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(soporteService.verDetalles(id, authentication.getName()));
    }

    @GetMapping("/mis-tickets/{id}/mensajes")
    public ResponseEntity<List<MensajeSoporteDTO>> verMensajes(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(soporteService.listarMensajesTicketUsuario(id, authentication.getName()));
    }

    @PostMapping("/mis-tickets/{id}/mensajes")
    public ResponseEntity<MensajeSoporteDTO> enviarMensaje(
            @PathVariable Long id,
            @Valid @RequestBody CrearMensajeSoporteDTO dto,
            Authentication authentication) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(soporteService.enviarMensajeUsuario(id, dto, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarTicket(@PathVariable Long id, Authentication authentication) {

        soporteService.borrarTicket(id, authentication.getName());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}