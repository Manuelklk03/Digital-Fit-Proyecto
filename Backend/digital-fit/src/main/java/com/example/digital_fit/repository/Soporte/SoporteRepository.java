package com.example.digital_fit.repository.Soporte;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.Soporte.Soporte;

@Repository
public interface SoporteRepository extends JpaRepository<Soporte, Long> {
    List<Soporte> findByUsuarioOrderByFechaDesc(Usuario usuario);

    // Para panel de admin:
    List<Soporte> findAllByOrderByFechaDesc();
}
