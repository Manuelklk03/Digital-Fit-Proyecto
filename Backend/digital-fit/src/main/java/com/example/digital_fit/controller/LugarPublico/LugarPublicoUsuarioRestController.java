package com.example.digital_fit.controller.LugarPublico;

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

import com.example.digital_fit.dto.LugarPublico.CrearLugarPublicoDTO;
import com.example.digital_fit.dto.LugarPublico.LugarPublicoUsuarioDTO;
import com.example.digital_fit.service.LugarPublico.LugarPublicoUsuarioService;

@RestController
@RequestMapping("/api/mis-lugares-publicos")
public class LugarPublicoUsuarioRestController {

    @Autowired
    private LugarPublicoUsuarioService lugarPublicoUsuarioService;

    // Listar mis lugares publicos:
    @GetMapping
    public ResponseEntity<List<LugarPublicoUsuarioDTO>> listar(Authentication authentication) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(lugarPublicoUsuarioService.listarLugares(authentication.getName()));
    }

    // Ver detalles:
    @GetMapping("/{id}")
    public ResponseEntity<LugarPublicoUsuarioDTO> obtenerPorId(@PathVariable Long id, Authentication authentication) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(lugarPublicoUsuarioService.verDetalle(id, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {

        lugarPublicoUsuarioService.borrarLugar(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Guardar desde G-MAPS:
    @PostMapping
    public ResponseEntity<LugarPublicoUsuarioDTO> crear(@RequestBody CrearLugarPublicoDTO dto,
            Authentication authentication) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(lugarPublicoUsuarioService.guardarDesdeMaps(dto, authentication.getName()));
    }

    // Guardar desde base:
    @PostMapping("/base/{idBase}")
    public ResponseEntity<LugarPublicoUsuarioDTO> crearDesdeBase(@PathVariable Long idBase,
            Authentication authentication) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(lugarPublicoUsuarioService.guardarDesdeBase(idBase, authentication.getName()));
    }
}
