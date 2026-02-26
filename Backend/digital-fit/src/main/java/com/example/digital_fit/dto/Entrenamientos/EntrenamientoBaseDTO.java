package com.example.digital_fit.dto.Entrenamientos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EntrenamientoBaseDTO {

    private Long id;

    private String nombre;

    private String descripcion;
}
