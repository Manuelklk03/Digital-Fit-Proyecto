package com.example.digital_fit.dto.Soporte;

import java.time.LocalDateTime;

import com.example.digital_fit.model.Enums.EstadoSoporte;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({ "id", "asunto", "mensaje", "fecha", "estado", "usuarioId", "username" })
public class SoporteDTO {

    private Long id;

    private String asunto;

    private String mensaje;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fecha;

    private EstadoSoporte estado;

    private Long usuarioId;

    private String username;
}