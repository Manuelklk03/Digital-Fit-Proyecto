package com.example.digital_fit.controller.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.digital_fit.dto.Admin.CrearAdmin;
import com.example.digital_fit.service.Auth.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    private AuthService authService;

    @PostMapping("/crear-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> crearAdmin(@Valid @RequestBody CrearAdmin crearAdmin) {

        authService.registrarAdmin(crearAdmin);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Admin: " + crearAdmin.getUsername() + " creado exitosamente");
    }
}
