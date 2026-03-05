package com.example.digital_fit.model.Valoracion;

import java.time.LocalDateTime;

import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.Enums.TipoDeValoracion;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "valoraciones")
public class Valoracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    private Integer puntuacion;

    private String comentario;

    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    private TipoDeValoracion tipoDeValoracion;

    private Long idRelacionado; // ID del centro, lugar público o entrenamiento relacionado
}
