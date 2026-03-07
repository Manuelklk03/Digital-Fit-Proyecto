package com.example.digital_fit.model.Entrenamientos;

import java.time.LocalDateTime;

import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.CentroPrivado.CentroPrivadoUsuario;
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

    // Usuario que realizo el entrenamiento
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Entrenamiento base de la app (Opcional)
    @ManyToOne
    @JoinColumn(name = "entrenamiento_base_id")
    private EntrenamientoBase entrenamientoBase;

    // Entrnamiento creado o guardado por el usuario (Opcional)
    @ManyToOne
    @JoinColumn(name = "entrenamiento_usuario_id")
    private EntrenamientoUsuario entrenamientoUsuario;

    // Lugar publico donde se realizo el entrenamiento (Opcional)
    @ManyToOne
    @JoinColumn(name = "lugar_publico_id")
    private LugarPublicoUsuario lugarPublico;

    // Centro privado donde se realizo el entrenamiento (Opcional)
    @ManyToOne
    @JoinColumn(name = "centro_privado_id")
    private CentroPrivadoUsuario centroPrivado;

    // Fecha y hora del entrenamiento
    private LocalDateTime fechaHora;

    private Integer duracionMinutos; // Duración del entrenamiento en minutos

    // Notas del usuario:
    @Column(length = 2000)
    private String notas;
}
