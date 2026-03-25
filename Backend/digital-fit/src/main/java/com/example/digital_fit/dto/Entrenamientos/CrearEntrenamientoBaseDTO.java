package com.example.digital_fit.dto.Entrenamientos;

import lombok.NoArgsConstructor;

import com.example.digital_fit.model.Enums.CategoriaEntrenamientoComunidad;
import com.example.digital_fit.model.Enums.NivelEntrenamiento;

import lombok.Data;

@Data
@NoArgsConstructor
public class CrearEntrenamientoBaseDTO {

    private String nombre;

    private String descripcion;

    private CategoriaEntrenamientoComunidad categoria;

    private NivelEntrenamiento nivel;

    private Integer duracionEnMinutos;
}
