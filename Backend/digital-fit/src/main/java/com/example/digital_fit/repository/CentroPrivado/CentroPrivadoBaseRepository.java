package com.example.digital_fit.repository.CentroPrivado;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.digital_fit.model.CentroPrivado.CentroPrivadoBase;

@Repository
public interface CentroPrivadoBaseRepository extends JpaRepository<CentroPrivadoBase, Long> {

    List<CentroPrivadoBase> findByNombreContainingIgnoreCase(String nombre);

    List<CentroPrivadoBase> findByDireccionContainingIgnoreCase(String direccion);

    List<CentroPrivadoBase> findByPrecioMensualLessThanEqual(Double precioMensual);

    Optional<CentroPrivadoBase> findByNombreIgnoreCase(String nombre);
} 
