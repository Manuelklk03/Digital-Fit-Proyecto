package com.example.digital_fit.controller.CentroPrivado;

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

import com.example.digital_fit.dto.CentroPrivado.CentroPrivadoUsuarioDTO;
import com.example.digital_fit.dto.CentroPrivado.CrearCentroPrivadoDTO;
import com.example.digital_fit.service.CentroPrivado.CentroPrivadoUsuarioService;

@RestController
@RequestMapping("/api/mis-centros-privados")
public class CentroPrivadoUsuarioRestController {

    @Autowired
    private CentroPrivadoUsuarioService centroPrivadoUsuarioService;

    // Listar:
    @GetMapping
    public ResponseEntity<List<CentroPrivadoUsuarioDTO>> listar(Authentication authentication,
            @RequestParam(required = false) String nombre, @RequestParam(required = false) String direccion,
            @RequestParam(required = false) Double precioMensual) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(centroPrivadoUsuarioService.listarOFiltrar(authentication.getName(), nombre, direccion,
                        precioMensual));
    }

    // Detalle:
    @GetMapping("/{id}")
    public ResponseEntity<CentroPrivadoUsuarioDTO> obtenerPorId(@PathVariable Long id, Authentication authentication) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(centroPrivadoUsuarioService.verDetalle(id, authentication.getName()));
    }

    // Añadir desde app:
    @PostMapping("/centros-app/{id}")
    public ResponseEntity<CentroPrivadoUsuarioDTO> AñadirPrivadoAMisCentros(@PathVariable Long id,
            Authentication authentication) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(centroPrivadoUsuarioService.AñadirPrivadoAMisCentros(id, authentication.getName()));
    }

    // Añadir desde maps (Angular) :
    @PostMapping("/centros-maps")
    public ResponseEntity<CentroPrivadoUsuarioDTO> AñadirCentroPrivadoMaps(@RequestBody CrearCentroPrivadoDTO dto,
            Authentication authentication) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(centroPrivadoUsuarioService.AñdirCentroPrivadoMaps(dto, authentication.getName()));
    }

    // Borrar:
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarDeMisCentrosGuardados(@PathVariable Long id, Authentication authentication) {

        centroPrivadoUsuarioService.borrarDeMisCentrosGuardados(id, authentication.getName());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
