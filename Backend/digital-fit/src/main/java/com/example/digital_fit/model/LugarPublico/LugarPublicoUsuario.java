package com.example.digital_fit.model.LugarPublico;

import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.Enums.TipoLugarPublico;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lugares_publicos_usuario")
@Builder
public class LugarPublicoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String direccion;

    @Column(length = 1000)
    private String descripcion;

    private String telefono;

    private String horario;

    private Double latitud;

    private Double longitud;

    @Enumerated(EnumType.STRING)
    private TipoLugarPublico tipo;

    // Relacion:
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
