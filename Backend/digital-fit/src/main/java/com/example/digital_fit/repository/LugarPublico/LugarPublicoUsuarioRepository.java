package com.example.digital_fit.repository.LugarPublico;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.digital_fit.model.LugarPublico.LugarPublicoUsuario;
import com.example.digital_fit.model.Auth.Usuario;

@Repository
public interface LugarPublicoUsuarioRepository extends JpaRepository<LugarPublicoUsuario, Long> {
    List<LugarPublicoUsuario> findByUsuario(Usuario usuario); 
}
