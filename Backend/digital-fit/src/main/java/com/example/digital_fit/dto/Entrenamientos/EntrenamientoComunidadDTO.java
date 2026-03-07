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
@JsonPropertyOrder({ "id", "nombre", "descripcion", "categoria", "nivel", "duracionEnMinutos", "usuario", "fechaPublicacion" })
public class EntrenamientoComunidadDTO {

    private Long id;

    private String nombre;

    private String descripcion;

    private CategoriaEntrenamientoComunidad categoria;

    private NivelEntrenamiento nivel;

    private Integer duracionEnMinutos;

    private String usuario;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaPublicacion;
}
