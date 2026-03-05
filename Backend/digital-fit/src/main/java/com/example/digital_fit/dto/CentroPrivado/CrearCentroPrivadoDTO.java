package com.example.digital_fit.dto.CentroPrivado;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearCentroPrivadoDTO {

    private String nombre;

    private String direccion;

    private String telefono;

    private String horario;

    private double precioMensual;

    private String descripcion;
    
    private double latitud;

    private double longitud;
}
