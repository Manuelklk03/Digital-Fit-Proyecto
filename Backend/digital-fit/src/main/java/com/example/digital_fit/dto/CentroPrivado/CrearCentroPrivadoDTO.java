package com.example.digital_fit.dto.CentroPrivado;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearCentroPrivadoDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    private String telefono;

    @NotBlank(message = "El horario es obligatorio")
    private String horario;

    @NotBlank(message = "El precio mensual es obligatorio")
    @Min(value = 0, message = "El precio mensual debe ser mayor que 0")
    private double precioMensual;

    @Max(value = 1500, message = "La descripción no puede tener más de 1500 caracteres")
    private String descripcion;

    private double latitud;

    private double longitud;
}
