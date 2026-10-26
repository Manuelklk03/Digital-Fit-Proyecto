package com.example.digital_fit.dto.Soporte;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearSoporteDTO {

    @NotBlank(message = "El asunto es obligatorio")
    @Size(max = 100, message = "El asunto no puede tener más de 100 caracteres")
    private String asunto;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;
}
