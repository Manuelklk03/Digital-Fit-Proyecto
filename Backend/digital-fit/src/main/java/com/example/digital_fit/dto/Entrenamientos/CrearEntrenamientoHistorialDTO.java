package com.example.digital_fit.dto.Entrenamientos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime fecha;

    @NotNull(message = "La duración es obligatoria")
    @Min(value = 1, message = "La duración debe ser mayor que 0")
    private Integer duracionEnMinutos;

    @Size(max = 1500, message = "Las notas no pueden tener más de 1500 caracteres")
    private String notas;
}
