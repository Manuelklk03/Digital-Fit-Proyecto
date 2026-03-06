package com.example.digital_fit.dto.Entrenamientos;

import com.example.digital_fit.model.Enums.CategoriaEntrenamientoComunidad;
import com.example.digital_fit.model.Enums.NivelEntrenamiento;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({
        "id",
        "nombre",
        "descripcion",
        "categoria",
        "nivel",
        "duracionEnMinutos"
})
public class EntrenamientoBaseDTO {

    private Long id;

    private String nombre;

    private String descripcion;

    private CategoriaEntrenamientoComunidad categoria;

    private NivelEntrenamiento nivel;

    private Integer duracionEnMinutos;
}
