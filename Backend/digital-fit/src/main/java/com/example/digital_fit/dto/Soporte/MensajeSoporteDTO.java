package com.example.digital_fit.dto.Soporte;

import java.time.LocalDateTime;

import com.example.digital_fit.model.Enums.Rol;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MensajeSoporteDTO {

    private Long id;

    private String contenido;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fecha;

    private Long emisorId;

    private String emisorUsername;

    private Rol emisorRol;
}