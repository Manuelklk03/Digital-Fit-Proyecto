package com.example.digital_fit.repository.CentroPrivado;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.CentroPrivado.CentroPrivadoUsuario;

@Repository
public interface CentroPrivadoUsuarioRepository extends JpaRepository<CentroPrivadoUsuario, Long> {
    List<CentroPrivadoUsuario> findByUsuario(Usuario usuario);

    List<CentroPrivadoUsuario> findByUsuarioAndNombreContainingIgnoreCase(Usuario usuario, String nombre);

    List<CentroPrivadoUsuario> findByUsuarioAndUsuarioAndDireccionContainingIgnoreCase(Usuario usuario,
            String direccion);

    List<CentroPrivadoUsuario> findByUsuarioAndPrecioMensualLessThanEqual(Usuario usuario, Double precioMensual);
}
