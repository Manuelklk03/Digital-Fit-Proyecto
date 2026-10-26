package com.example.digital_fit.controller.Valoracion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    // Ver mis valoraciones
    @GetMapping("/mis-valoraciones")
    public ResponseEntity<List<ValoracionDTO>> verMisValoraciones(Authentication authentication) {
        return ResponseEntity.ok(valoracionService.listarMisValoraciones(authentication.getName()));
    }

    // Ver valoraciones de un contenido específico
    @GetMapping("/{tipoContenido}/{idContenido}")
    public ResponseEntity<List<ValoracionDTO>> verValoracionesPorContenido(
            @PathVariable TipoDeValoracion tipoContenido, @PathVariable Long idContenido) {
        return ResponseEntity.ok(valoracionService.listarPorContenido(tipoContenido, idContenido));
    }

    // Crear o actualizar una valoración
    @PostMapping("/{tipoContenido}/{idContenido}")
    public ResponseEntity<ValoracionDTO> crearOActualizar(@PathVariable TipoDeValoracion tipoContenido,
            @PathVariable Long idContenido, @RequestBody CrearValoracionDTO crearValoracionDTO,
            Authentication authentication) {

        return ResponseEntity.ok(valoracionService.crearOActualizar(tipoContenido, idContenido,
                crearValoracionDTO, authentication.getName()));
    }

    // Eliminar una valoración
    @DeleteMapping("/{tipoContenido}/{idContenido}")
    public ResponseEntity<Void> eliminarValoracion(@PathVariable TipoDeValoracion tipoContenido,
            @PathVariable Long idContenido, Authentication authentication) {

        valoracionService.borrarMiValoracion(tipoContenido, idContenido, authentication.getName());

        return ResponseEntity.noContent().build();
    }

}
