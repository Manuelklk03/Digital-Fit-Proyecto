package com.example.digital_fit.model.Entrenamientos;

import java.time.LocalDateTime;

import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.CentroPrivado.CentroPrivadoBase;
import com.example.digital_fit.model.CentroPrivado.CentroPrivadoUsuario;
import com.example.digital_fit.model.LugarPublico.LugarPublicoBase;
import com.example.digital_fit.model.LugarPublico.LugarPublicoUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Data
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "historial_entrenamientos")
public class HistorialEntrenamientos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuario que realizó el entrenamiento
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Entrenamiento base
    @ManyToOne
    @JoinColumn(name = "entrenamiento_base_id")
    private EntrenamientoBase entrenamientoBase;

    // Entrenamiento del usuario
    @ManyToOne
    @JoinColumn(name = "entrenamiento_usuario_id")
    private EntrenamientoUsuario entrenamientoUsuario;

    // Lugar público base
    @ManyToOne
    @JoinColumn(name = "lugar_publico_base_id")
    private LugarPublicoBase lugarPublicoBase;

    // Lugar público del usuario
    @ManyToOne
    @JoinColumn(name = "lugar_publico_usuario_id")
    private LugarPublicoUsuario lugarPublicoUsuario;

    // Centro privado base
    @ManyToOne
    @JoinColumn(name = "centro_privado_base_id")
    private CentroPrivadoBase centroPrivadoBase;

    // Centro privado del usuario
    @ManyToOne
    @JoinColumn(name = "centro_privado_usuario_id")
    private CentroPrivadoUsuario centroPrivadoUsuario;

    // Fecha y hora
    private LocalDateTime fechaHora;

    private Integer duracionMinutos;

    @Column(length = 2000)
    private String notas;
}