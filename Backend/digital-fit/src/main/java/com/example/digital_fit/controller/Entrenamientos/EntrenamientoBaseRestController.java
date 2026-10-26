package com.example.digital_fit.controller.Entrenamientos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.digital_fit.dto.Entrenamientos.EntrenamientoBaseDTO;
import com.example.digital_fit.model.Enums.CategoriaEntrenamientoComunidad;
import com.example.digital_fit.model.Enums.NivelEntrenamiento;
import com.example.digital_fit.service.Entrenamientos.EntrenamientoBaseService;

@RestController
@RequestMapping("/api/entrenamientos")
public class EntrenamientoBaseRestController {

    @Autowired
    private EntrenamientoBaseService entrenamientoBaseService;

    @GetMapping
    public ResponseEntity<List<EntrenamientoBaseDTO>> listarOFiltrar(
            @RequestParam(required = false) CategoriaEntrenamientoComunidad categoria,
            @RequestParam(required = false) NivelEntrenamiento nivel,
            @RequestParam(required = false) Integer duracionEnMinutos,
            @RequestParam(required = false) String nombre) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(entrenamientoBaseService.listarOFiltrar(categoria, nivel, duracionEnMinutos, nombre));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntrenamientoBaseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(entrenamientoBaseService.obtenerPorId(id));
    }
}
