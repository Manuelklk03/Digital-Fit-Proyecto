package com.example.digital_fit.dto.Estadisticas;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({ "entrenamientosRealizados", "minutosEntrenados", "centrosPrivadosVisitados",
        "lugaresPublicosVisitados", "entrenamientoMasRealizado" })
public class EstadisticaUsuarioDTO {

    private Long entrenamientosRealizados;

    private Integer minutosEntrenados;

    private Long centrosPrivadosVisitados;

    private Long lugaresPublicosVisitados;

    private String entrenamientoMasRealizado;
}
