package com.example.digital_fit.model.Entrenamientos;

import java.time.LocalDateTime;

import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.Enums.CategoriaEntrenamientoComunidad;
import com.example.digital_fit.model.Enums.NivelEntrenamiento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "entrenamientos_comunidad")
public class EntrenamientoComunidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(length = 2000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private CategoriaEntrenamientoComunidad categoria;

    @Enumerated(EnumType.STRING)
    private NivelEntrenamiento nivel;

    private Integer duracionEnMinutos;

    private LocalDateTime fechaPublicacion;

    // Relaciones con Usuario (El que subió el entrenamiento)
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
