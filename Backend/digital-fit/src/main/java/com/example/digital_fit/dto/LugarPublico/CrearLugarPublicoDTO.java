package com.example.digital_fit.dto.LugarPublico;

import com.example.digital_fit.model.Enums.TipoLugarPublico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearLugarPublicoDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @Size(max = 1500, message = "La descripción no puede tener más de 1500 caracteres")
    private String descripcion;

    private String telefono;

    private String horario;

    @NotNull(message = "La latitud es obligatoria")
    private Double latitud;

    @NotNull(message = "La longitud es obligatoria")
    private Double longitud;

    @NotNull(message = "El tipo es obligatorio")
    private TipoLugarPublico tipo;
}