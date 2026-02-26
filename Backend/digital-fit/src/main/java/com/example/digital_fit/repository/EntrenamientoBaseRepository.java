package com.example.digital_fit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.digital_fit.model.EntrenamientoBase;

@Repository
public interface EntrenamientoBaseRepository extends JpaRepository<EntrenamientoBase, Long> {

}
