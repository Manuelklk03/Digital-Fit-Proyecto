package com.example.digital_fit.controller.Entrenamientos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.digital_fit.dto.Entrenamientos.EntrenamientoBaseDTO;
import com.example.digital_fit.service.Entrenamientos.EntrenamientoBaseService;

@RestController
@RequestMapping("/api/entrenamientos")
public class EntrenamientoBaseRestController {

    @Autowired
    private EntrenamientoBaseService entrenamientoBaseService;

    @GetMapping
    public ResponseEntity<List<EntrenamientoBaseDTO>> listarTodos() {
        return ResponseEntity.status(HttpStatus.OK).body(entrenamientoBaseService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntrenamientoBaseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(entrenamientoBaseService.obtenerPorId(id));
    }
}
