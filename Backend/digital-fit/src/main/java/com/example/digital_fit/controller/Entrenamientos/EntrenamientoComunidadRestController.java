package com.example.digital_fit.controller.Entrenamientos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.digital_fit.dto.Entrenamientos.EntrenamientoComunidadDTO;
import com.example.digital_fit.service.Entrenamientos.EntrenamientoComunidadService;

@RestController
@RequestMapping("/api/entrenamientos-comunidad")
public class EntrenamientoComunidadRestController {

    @Autowired
    private EntrenamientoComunidadService entrenamientoComunidadService;

    @GetMapping
    public List<EntrenamientoComunidadDTO> listarEntrenamientosComunidad() {
        return entrenamientoComunidadService.listarTodos();
    }
}
