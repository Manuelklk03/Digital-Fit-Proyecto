package com.example.digital_fit.controller.LugarPublico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.digital_fit.dto.LugarPublico.LugarPublicoBaseDTO;
import com.example.digital_fit.service.LugarPublico.LugarPublicoBaseService;

@RestController
@RequestMapping("/api/lugares-publicos")
public class LugarPublicoBaseRestController {

    @Autowired
    private LugarPublicoBaseService lugarPublicoBaseService;

    @GetMapping
    public ResponseEntity<List<LugarPublicoBaseDTO>> listar() {
        return ResponseEntity.ok(lugarPublicoBaseService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LugarPublicoBaseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(lugarPublicoBaseService.buscarPorId(id));
    }
}
