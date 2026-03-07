package com.example.digital_fit.repository.Entrenamientos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.Entrenamientos.HistorialEntrenamientos;

@Repository
public interface HistorialEntrenamientosRepository extends JpaRepository<HistorialEntrenamientos, Long> {

    List<HistorialEntrenamientos> findByUsuarioOrderByFechaHoraDesc(Usuario usuario);
}
