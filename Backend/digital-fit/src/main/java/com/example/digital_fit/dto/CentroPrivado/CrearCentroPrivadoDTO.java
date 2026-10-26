package com.example.digital_fit.dto.CentroPrivado;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearCentroPrivadoDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    private String telefono;

    @NotBlank(message = "El horario es obligatorio")
    private String horario;

    @NotNull(message = "El precio mensual es obligatorio")
    @Min(value = 0, message = "El precio mensual debe ser mayor o igual que 0")
    private Double precioMensual;

    @Size(max = 1500, message = "La descripción no puede tener más de 1500 caracteres")
    private String descripcion;

    @NotNull(message = "La latitud es obligatoria")
    private Double latitud;

    @NotNull(message = "La longitud es obligatoria")
    private Double longitud;
}