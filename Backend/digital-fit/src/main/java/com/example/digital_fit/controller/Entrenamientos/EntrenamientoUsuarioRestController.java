package com.example.digital_fit.controller.Entrenamientos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.digital_fit.service.Entrenamientos.EntrenamientoUsuarioService;

@RestController
@RequestMapping("/api/mis-entrenamientos")
public class EntrenamientoUsuarioRestController {

    @Autowired
    private EntrenamientoUsuarioService entrenamientoUsuarioService;
}
