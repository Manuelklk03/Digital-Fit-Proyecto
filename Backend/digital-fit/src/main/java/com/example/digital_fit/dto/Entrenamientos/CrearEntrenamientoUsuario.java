package com.example.digital_fit.dto.Entrenamientos;

import com.example.digital_fit.model.Enums.CategoriaEntrenamientoComunidad;
import com.example.digital_fit.model.Enums.NivelEntrenamiento;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearEntrenamientoUsuario {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 1500, message = "La descripción no puede tener más de 1500 caracteres")
    private String descripcion;

    @NotNull(message = "La categoría es obligatoria")
    private CategoriaEntrenamientoComunidad categoria;

    @NotNull(message = "El nivel es obligatorio")
    private NivelEntrenamiento nivel;

    @NotNull(message = "La duración es obligatoria")
    @Min(value = 1, message = "La duración debe ser mayor que 0")
    private Integer duracionEnMinutos;
}
