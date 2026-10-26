package com.example.digital_fit.dto.Entrenamientos;

import com.example.digital_fit.model.Enums.CategoriaEntrenamientoComunidad;
import com.example.digital_fit.model.Enums.NivelEntrenamiento;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearEntrenamientoUsuario {

    private String nombre;

    private String descripcion;

    private CategoriaEntrenamientoComunidad categoria;

    private NivelEntrenamiento nivel;

    private Integer duracionEnMinutos;
}
