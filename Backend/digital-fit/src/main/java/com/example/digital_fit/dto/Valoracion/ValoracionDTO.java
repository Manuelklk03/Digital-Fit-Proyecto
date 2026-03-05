package com.example.digital_fit.dto.Valoracion;

import java.time.LocalDateTime;

import com.example.digital_fit.model.Enums.TipoDeValoracion;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValoracionDTO {
    private Long id;

    private String username;

    private Integer puntuacion;

    private String comentario;

    private LocalDateTime fecha;

    private TipoDeValoracion tipoDeValoracion;

    private Long idRelacionado; // ID del centro, lugar público o entrenamiento relacionado
}
