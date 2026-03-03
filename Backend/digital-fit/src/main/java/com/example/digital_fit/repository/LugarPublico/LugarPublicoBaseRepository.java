package com.example.digital_fit.repository.LugarPublico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.digital_fit.model.LugarPublico.LugarPublicoBase;

@Repository
public interface LugarPublicoBaseRepository extends JpaRepository<LugarPublicoBase, Long> {

}
