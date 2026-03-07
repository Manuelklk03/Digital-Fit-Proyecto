package com.example.digital_fit.dto.Entrenamientos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({ "id", "entrenamiento", "lugar", "fecha", "duracionEnMinutos", "notas" })
public class HistorialEntrenamientosDTO {

    private Long id;

    private String entrenamiento;

    private String lugar;

    private LocalDateTime fecha;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Integer duracionEnMinutos;

    private String notas;
}
