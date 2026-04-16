package com.example.digital_fit.controller.Valoracion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.digital_fit.dto.Valoracion.CrearValoracionDTO;
import com.example.digital_fit.dto.Valoracion.ValoracionDTO;
import com.example.digital_fit.model.Enums.TipoDeValoracion;
import com.example.digital_fit.service.Valoracion.ValoracionService;

@RestController
@RequestMapping("/api/valoraciones")
public class ValoracionRestController {

    @Autowired
    private ValoracionService valoracionService;

    @GetMapping("/mis-valoraciones")
    public ResponseEntity<List<ValoracionDTO>> verMisValoraciones(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(valoracionService.listarMisValoraciones(authentication.getName()));
    }

    @GetMapping("/publicas")
    public ResponseEntity<List<ValoracionDTO>> verValoracionesPublicas(
            @RequestParam(required = false) String categoria) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(valoracionService.listarValoracionesPublicas(categoria));
    }

    @GetMapping("/{tipoContenido}/{idContenido}")
    public ResponseEntity<List<ValoracionDTO>> verValoracionesPorContenido(
            @PathVariable TipoDeValoracion tipoContenido, @PathVariable Long idContenido) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(valoracionService.listarPorContenido(tipoContenido, idContenido));
    }

    @PostMapping("/{tipoContenido}/{idContenido}")
    public ResponseEntity<ValoracionDTO> crearOActualizar(@PathVariable TipoDeValoracion tipoContenido,
            @PathVariable Long idContenido, @RequestBody CrearValoracionDTO crearValoracionDTO,
            Authentication authentication) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(valoracionService.crearOActualizar(tipoContenido, idContenido,
                        crearValoracionDTO, authentication.getName()));
    }

    @DeleteMapping("/{tipoContenido}/{idContenido}")
    public ResponseEntity<Void> eliminarValoracion(@PathVariable TipoDeValoracion tipoContenido,
            @PathVariable Long idContenido, Authentication authentication) {

        valoracionService.borrarMiValoracion(tipoContenido, idContenido, authentication.getName());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}