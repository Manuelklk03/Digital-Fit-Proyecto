package com.example.digital_fit.controller.LugarPublico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.digital_fit.dto.LugarPublico.LugarPublicoBaseDTO;
import com.example.digital_fit.model.Enums.TipoLugarPublico;
import com.example.digital_fit.service.LugarPublico.LugarPublicoBaseService;

@RestController
@RequestMapping("/api/lugares-publicos")
public class LugarPublicoBaseRestController {

    @Autowired
    private LugarPublicoBaseService lugarPublicoBaseService;

    @GetMapping
    public ResponseEntity<List<LugarPublicoBaseDTO>> listar(@RequestParam(required = false) String nombre,
            @RequestParam(required = false) String direccion, @RequestParam(required = false) TipoLugarPublico tipo) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(lugarPublicoBaseService.listarOFiltrar(nombre, direccion, tipo));

    }

    @GetMapping("/{id}")
    public ResponseEntity<LugarPublicoBaseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(lugarPublicoBaseService.buscarPorId(id));
    }
}
