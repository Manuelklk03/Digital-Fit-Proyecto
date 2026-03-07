package com.example.digital_fit.controller.CentroPrivado;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.digital_fit.dto.CentroPrivado.CentroPrivadoBaseDTO;
import com.example.digital_fit.service.CentroPrivado.CentroPrivadoBaseService;

@RestController
@RequestMapping("/api/centros-privados")
public class CentroPrivadoBaseRestController {

    @Autowired
    private CentroPrivadoBaseService centroPrivadoBaseService;

    @GetMapping
    public ResponseEntity<List<CentroPrivadoBaseDTO>> listar() {
        return ResponseEntity.status(HttpStatus.OK).body(centroPrivadoBaseService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CentroPrivadoBaseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(centroPrivadoBaseService.obtenerPorId(id));
    }
}
