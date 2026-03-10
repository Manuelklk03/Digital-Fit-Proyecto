package com.example.digital_fit.repository.LugarPublico;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.digital_fit.model.Enums.TipoLugarPublico;
import com.example.digital_fit.model.LugarPublico.LugarPublicoBase;

@Repository
public interface LugarPublicoBaseRepository extends JpaRepository<LugarPublicoBase, Long> {

    List<LugarPublicoBase> findByNombreContainingIgnoreCase(String nombre);

    List<LugarPublicoBase> findByDireccionContainingIgnoreCase(String direccion);

    List<LugarPublicoBase> findByTipo(TipoLugarPublico tipoLugarPublico);

}
