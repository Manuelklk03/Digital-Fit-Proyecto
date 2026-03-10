package com.example.digital_fit.controller.Estadisticas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.digital_fit.dto.Estadisticas.EstadisticaUsuarioDTO;
import com.example.digital_fit.service.Estadisticas.EstadisticasService;

@RestController
@RequestMapping("/api/estadisticas")
public class EstadisticasRestController {

    @Autowired
    private EstadisticasService estadisticasService;

    @GetMapping
    public ResponseEntity<EstadisticaUsuarioDTO> obtenerEstadisticas(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(estadisticasService.obtenerEstadisticas(authentication.getName()));
    }
}
