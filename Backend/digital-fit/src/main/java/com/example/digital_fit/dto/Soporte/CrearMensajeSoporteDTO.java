package com.example.digital_fit.dto.Soporte;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearMensajeSoporteDTO {

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 2000, message = "El mensaje no puede tener más de 2000 caracteres")
    private String contenido;
}