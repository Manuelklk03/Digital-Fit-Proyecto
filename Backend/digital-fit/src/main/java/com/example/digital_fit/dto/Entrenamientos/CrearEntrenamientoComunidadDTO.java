package com.example.digital_fit.dto.Entrenamientos;

import java.time.LocalDateTime;

import com.example.digital_fit.model.Enums.CategoriaEntrenamientoComunidad;
import com.example.digital_fit.model.Enums.NivelEntrenamiento;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({ "nombre", "descripcion", "categoria", "nivel", "duracionEnMinutos", "fechaPublicacion" })
public class CrearEntrenamientoComunidadDTO {

    private String nombre;

    private String descripcion;

    private CategoriaEntrenamientoComunidad categoria;

    private NivelEntrenamiento nivel;

    private Integer duracionEnMinutos;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaPublicacion;
}
