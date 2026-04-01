package com.example.digital_fit.repository.LugarPublico;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.Enums.TipoLugarPublico;
import com.example.digital_fit.model.LugarPublico.LugarPublicoUsuario;

@Repository
public interface LugarPublicoUsuarioRepository extends JpaRepository<LugarPublicoUsuario, Long> {

    List<LugarPublicoUsuario> findByUsuario(Usuario usuario);

    List<LugarPublicoUsuario> findByUsuarioAndNombreContainingIgnoreCase(Usuario usuario, String nombre);

    List<LugarPublicoUsuario> findByUsuarioAndDireccionContainingIgnoreCase(Usuario usuario, String direccion);

    List<LugarPublicoUsuario> findByUsuarioAndTipo(Usuario usuario, TipoLugarPublico tipoLugarPublico);

    boolean existsByUsuarioAndNombreIgnoreCaseAndDireccionIgnoreCaseAndTipo(
            Usuario usuario,
            String nombre,
            String direccion,
            TipoLugarPublico tipo
    );
}