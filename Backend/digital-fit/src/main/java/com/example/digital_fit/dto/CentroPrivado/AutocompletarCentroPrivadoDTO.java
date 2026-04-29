package com.example.digital_fit.dto.CentroPrivado;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AutocompletarCentroPrivadoDTO {

    private String direccion;
    private String telefono;
    private String horario;
    private Double precioMensual;
    private String descripcion;
    private Double latitud;
    private Double longitud;
    private boolean datosEncontrados;
}