package com.example.digital_fit.dto.Valoracion;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearValoracionDTO {

    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación debe ser mayor que 0")
    @Max(value = 5, message = "La puntuación debe ser menor o igual que 5")
    private Integer puntuacion;

    @Size(max = 1500, message = "El comentario no puede tener más de 1500 caracteres")
    private String comentario;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fecha;
}
