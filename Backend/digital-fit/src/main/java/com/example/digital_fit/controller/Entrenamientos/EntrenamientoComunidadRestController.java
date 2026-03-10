package com.example.digital_fit.controller.Entrenamientos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.digital_fit.dto.Entrenamientos.CrearEntrenamientoComunidadDTO;
import com.example.digital_fit.dto.Entrenamientos.EntrenamientoComunidadDTO;
import com.example.digital_fit.model.Enums.CategoriaEntrenamientoComunidad;
import com.example.digital_fit.model.Enums.NivelEntrenamiento;
import com.example.digital_fit.service.Entrenamientos.EntrenamientoComunidadService;

@RestController
@RequestMapping("/api/entrenamientos-comunidad")
public class EntrenamientoComunidadRestController {

    @Autowired
    private EntrenamientoComunidadService entrenamientoComunidadService;

    @GetMapping
    public ResponseEntity<List<EntrenamientoComunidadDTO>> listarOFiltrar(
            @RequestParam(required = false) CategoriaEntrenamientoComunidad categoria,
            @RequestParam(required = false) NivelEntrenamiento nivel,
            @RequestParam(required = false) Integer duracionEnMinutos, @RequestParam(required = false) String nombre) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(entrenamientoComunidadService.listarOFiltrar(categoria, nivel, duracionEnMinutos, nombre));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntrenamientoComunidadDTO> verDetalles(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(entrenamientoComunidadService.verDetalles(id));
    }

    @PostMapping
    public ResponseEntity<EntrenamientoComunidadDTO> crearNuevo(
            @RequestBody CrearEntrenamientoComunidadDTO entrenamientoComunidadDTO, Authentication authentication) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(entrenamientoComunidadService.crearEntrenamiento(entrenamientoComunidadDTO,
                        authentication.getName()));
    }

}
