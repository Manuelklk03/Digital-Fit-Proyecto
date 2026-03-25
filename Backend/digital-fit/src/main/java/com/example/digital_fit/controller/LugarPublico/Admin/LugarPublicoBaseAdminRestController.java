package com.example.digital_fit.controller.LugarPublico.Admin;

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

import com.example.digital_fit.dto.LugarPublico.CrearLugarPublicoDTO;
import com.example.digital_fit.dto.LugarPublico.LugarPublicoBaseDTO;
import com.example.digital_fit.service.LugarPublico.LugarPublicoBaseService;

@RestController
@RequestMapping("/api/admin/lugares-publicos-base")
public class LugarPublicoBaseAdminRestController {

    @Autowired
    private LugarPublicoBaseService lugarPublicoBaseService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LugarPublicoBaseDTO> crearLugarPublicoBase(@RequestBody CrearLugarPublicoDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(lugarPublicoBaseService.crearLugarPublico(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LugarPublicoBaseDTO> actualizarLugarPublicoBase(@PathVariable Long id,
            @RequestBody CrearLugarPublicoDTO dto) {

        return ResponseEntity.status(HttpStatus.OK).body(lugarPublicoBaseService.actualizarLugarPublico(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> borrarLugarPublicoBase(@PathVariable Long id) {

        lugarPublicoBaseService.eliminarLugarPublico(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
