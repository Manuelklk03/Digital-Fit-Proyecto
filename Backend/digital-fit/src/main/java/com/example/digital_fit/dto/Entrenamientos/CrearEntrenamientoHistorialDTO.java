package com.example.digital_fit.dto.Entrenamientos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({ "entrenamientoBaseId", "entrenamientoUsuarioId", "lugarPublicoId", "centroPrivadoId", "fecha",
        "duracionEnMinutos", "notas" })
public class CrearEntrenamientoHistorialDTO {

    private Long entrenamientoBaseId;

    private Long entrenamientoUsuarioId;

    private Long lugarPublicoId;

    private Long centroPrivadoId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fecha;

    private Integer duracionEnMinutos;

    private String notas;
}
