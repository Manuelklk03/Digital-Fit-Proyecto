package com.example.digital_fit.controller.Entrenamientos;

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

import com.example.digital_fit.dto.Entrenamientos.CrearEntrenamientoUsuario;
import com.example.digital_fit.dto.Entrenamientos.EntrenamientoUsuarioDTO;
import com.example.digital_fit.service.Entrenamientos.EntrenamientoUsuarioService;

@RestController
@RequestMapping("/api/mis-entrenamientos")
public class EntrenamientoUsuarioRestController {

    @Autowired
    private EntrenamientoUsuarioService entrenamientoUsuarioService;

    @GetMapping
    public ResponseEntity<List<EntrenamientoUsuarioDTO>> listarMisEntrenamientos(Authentication authentication) {
        return ResponseEntity.ok(entrenamientoUsuarioService.listarMisEntrenamientos(authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<EntrenamientoUsuarioDTO> crearEntrenamientoUsuario(@RequestBody CrearEntrenamientoUsuario dto,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(entrenamientoUsuarioService.crearEntrenamientoPersonalizado(dto, authentication.getName()));

    }

    @PostMapping("desde-base/{idBase}")
    public ResponseEntity<EntrenamientoUsuarioDTO> AñadirEntrenamientoDesdeBase(@PathVariable Long idBase,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(entrenamientoUsuarioService.añadirDesdeBase(idBase, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntrenamientoUsuarioDTO> borrarEntrenamientoUsuario(@PathVariable Long id,
            Authentication authentication) {

        entrenamientoUsuarioService.borrarDeMisEntrenamientos(id, authentication.getName());

        return ResponseEntity.noContent().build();
    }
}
