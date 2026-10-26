package com.example.digital_fit.dto.Valoracion;

import java.time.LocalDateTime;

import com.example.digital_fit.model.Enums.TipoDeValoracion;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValoracionDTO {

    private Long id;

    private Integer puntuacion;

    private String comentario;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fecha;

    private TipoDeValoracion tipoDeValoracion;

    private Long contenidoId;

    private Long usuarioId;

    private String username;
}
