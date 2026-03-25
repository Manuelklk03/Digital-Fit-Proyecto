package com.example.digital_fit.controller.Entrenamientos.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.digital_fit.dto.Entrenamientos.CrearEntrenamientoBaseDTO;
import com.example.digital_fit.dto.Entrenamientos.EntrenamientoBaseDTO;
import com.example.digital_fit.service.Entrenamientos.EntrenamientoBaseService;

@RequestMapping("/api/admin/entrenamientos-base")
@RestController
public class EntrenamientoBaseAdminRestController {

    @Autowired
    private EntrenamientoBaseService entrenamientoBaseService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntrenamientoBaseDTO> crearEntrenamientoBase(@RequestBody CrearEntrenamientoBaseDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(entrenamientoBaseService.crearEntrenamientoBase(dto));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntrenamientoBaseDTO> actualizarEntrenamientoBase(@PathVariable Long id,
            @RequestBody CrearEntrenamientoBaseDTO dto) {

        return ResponseEntity.status(HttpStatus.OK).body(entrenamientoBaseService.actualizarEntrenamientoBase(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> borrarEntrenamientoBase(@PathVariable Long id) {

        entrenamientoBaseService.eliminarEntrenamientoBase(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
