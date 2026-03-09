package com.example.digital_fit.dto.Soporte.Admin;

import com.example.digital_fit.model.Enums.EstadoSoporte;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CambiarEstadoSoporte {

    private EstadoSoporte estado;
}
