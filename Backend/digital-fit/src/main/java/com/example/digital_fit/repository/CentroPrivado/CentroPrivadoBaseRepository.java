package com.example.digital_fit.repository.CentroPrivado;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.digital_fit.model.CentroPrivado.CentroPrivadoBase;

@Repository
public interface CentroPrivadoBaseRepository extends JpaRepository<CentroPrivadoBase, Long> {

}
