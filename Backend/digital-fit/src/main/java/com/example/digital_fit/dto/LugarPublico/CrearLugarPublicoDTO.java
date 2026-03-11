package com.example.digital_fit.dto.LugarPublico;

import com.example.digital_fit.model.Enums.TipoLugarPublico;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearLugarPublicoDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @Max(value = 1500, message = "La descripción no puede tener más de 1500 caracteres")
    private String descripcion;

    private String telefono;

    private String horario;

    private Double latitud;

    private Double longitud;

    @NotBlank(message = "El tipo es obligatorio")
    private TipoLugarPublico tipo;
}
