package com.example.digital_fit.dto.LugarPublico;

import com.example.digital_fit.model.Enums.TipoLugarPublico;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearLugarPublicoDTO {

    private String nombre;

    private String direccion;

    private String descripcion;

    private String telefono;

    private String horario;

    private Double latitud;

    private Double longitud;

    private TipoLugarPublico tipo;
}
