package com.example.digital_fit.model.Soporte;

import java.time.LocalDateTime;

import com.example.digital_fit.model.Auth.Usuario;

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

@Entity
@Table(name = "mensajes_soporte")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensajeSoporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String contenido;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Soporte ticket;

    @ManyToOne
    @JoinColumn(name = "emisor_id", nullable = false)
    private Usuario emisor;
}