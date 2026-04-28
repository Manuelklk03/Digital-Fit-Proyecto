package com.example.digital_fit.repository.Soporte;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.digital_fit.model.Enums.Rol;
import com.example.digital_fit.model.Soporte.MensajeSoporte;
import com.example.digital_fit.model.Soporte.Soporte;

@Repository
public interface MensajeSoporteRepository extends JpaRepository<MensajeSoporte, Long> {

    List<MensajeSoporte> findByTicketOrderByFechaAsc(Soporte ticket);

    boolean existsByTicketAndEmisor_Rol(Soporte ticket, Rol rol);
}