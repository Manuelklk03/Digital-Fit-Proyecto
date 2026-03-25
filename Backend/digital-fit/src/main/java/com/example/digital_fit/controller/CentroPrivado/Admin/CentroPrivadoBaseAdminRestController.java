package com.example.digital_fit.controller.CentroPrivado.Admin;

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

import com.example.digital_fit.dto.CentroPrivado.CentroPrivadoBaseDTO;
import com.example.digital_fit.dto.CentroPrivado.CrearCentroPrivadoDTO;
import com.example.digital_fit.service.CentroPrivado.CentroPrivadoBaseService;

@RestController
@RequestMapping("/api/admin/centro-privado-base")
public class CentroPrivadoBaseAdminRestController {

    @Autowired
    private CentroPrivadoBaseService centroPrivadoBaseAdminService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CentroPrivadoBaseDTO> crearCentroPrivadoBase(@RequestBody CrearCentroPrivadoDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(centroPrivadoBaseAdminService.crearCentroPrivado(dto));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CentroPrivadoBaseDTO> actualizarCentroPrivadoBase(@PathVariable Long id,
            @RequestBody CrearCentroPrivadoDTO dto) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(centroPrivadoBaseAdminService.actualizarCentroPrivado(id, dto));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> borrarCentroPrivadoBase(@PathVariable Long id) {

        centroPrivadoBaseAdminService.borrarCentroPrivado(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
