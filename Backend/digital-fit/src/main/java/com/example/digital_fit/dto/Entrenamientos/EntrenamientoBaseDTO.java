package com.example.digital_fit.dto.Entrenamientos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({ "id", "nombre", "descripcion" })
public class EntrenamientoBaseDTO {

    private Long id;

    private String nombre;

    private String descripcion;
}
