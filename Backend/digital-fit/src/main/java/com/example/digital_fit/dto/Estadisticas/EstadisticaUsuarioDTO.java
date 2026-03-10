package com.example.digital_fit.dto.Estadisticas;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({ "entrenamientosRealizados", "minutosEntrenados", "promedioMinutosEntrenamiento",
        "centrosPrivadosVisitados",
        "lugaresPublicosVisitados", "entrenamientoMasRealizado" })
public class EstadisticaUsuarioDTO {

    private Long entrenamientosRealizados;

    private Integer minutosEntrenados;

    private Double promedioMinutosEntrenamiento;

    private Long centrosPrivadosVisitados;

    private Long lugaresPublicosVisitados;

    private String entrenamientoMasRealizado;
}
