package com.example.digital_fit.dto.Valoracion;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearValoracionDTO {

    private Integer puntuacion;

    private String comentario;
}
