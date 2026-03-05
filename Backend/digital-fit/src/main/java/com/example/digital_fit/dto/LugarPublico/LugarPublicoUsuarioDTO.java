package com.example.digital_fit.dto.LugarPublico;

import com.example.digital_fit.model.Enums.TipoLugarPublico;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({ "id", "nombre", "direccion", "descripcion", "telefono", "horario", "latitud", "longitud", "tipo" })
public class LugarPublicoUsuarioDTO {

    private Long id;

    private String nombre;

    private String direccion;

    private String descripcion;

    private String telefono;

    private String horario;

    private Double latitud;

    private Double longitud;

    private TipoLugarPublico tipo;
}
