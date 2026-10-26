package com.example.digital_fit.service.CentroPrivado;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.digital_fit.dto.CentroPrivado.CentroPrivadoBaseDTO;
import com.example.digital_fit.dto.CentroPrivado.CentroPrivadoUsuarioDTO;
import com.example.digital_fit.dto.CentroPrivado.crearCentroPrivado;
import com.example.digital_fit.exception.OperacionNoPermitida;
import com.example.digital_fit.exception.RecursoNoEncontradoException;
import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.CentroPrivado.CentroPrivadoBase;
import com.example.digital_fit.model.CentroPrivado.CentroPrivadoUsuario;
import com.example.digital_fit.repository.Auth.UsuarioRepository;
import com.example.digital_fit.repository.CentroPrivado.CentroPrivadoBaseRepository;
import com.example.digital_fit.repository.CentroPrivado.CentroPrivadoUsuarioRepository;

@Service
public class CentroPrivadoUsuarioService {

    @Autowired
    private CentroPrivadoUsuarioRepository centroPrivadoUsuarioRepository;

    @Autowired
    private CentroPrivadoBaseRepository centroPrivadoBaseRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Listar mis centros:
    public List<CentroPrivadoUsuarioDTO> listarMisCentros(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        List<CentroPrivadoUsuario> centros = centroPrivadoUsuarioRepository.findByUsuario(usuario);

        List<CentroPrivadoUsuarioDTO> dtos = new ArrayList<>();

        for (CentroPrivadoUsuario centro : centros) {
            dtos.add(entityToDto(centro));
        }

        return dtos;
    }

    // Añadir centro privado desde maps:
    public CentroPrivadoUsuarioDTO AñdirCentroPrivadoMaps(crearCentroPrivado dto, String username) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        CentroPrivadoUsuario centro = new CentroPrivadoUsuario();
        centro.setNombre(dto.getNombre());
        centro.setDireccion(dto.getDireccion());
        centro.setTelefono(dto.getTelefono());
        centro.setHorario(dto.getHorario());
        centro.setPrecioMensual(dto.getPrecioMensual());
        centro.setDescripcion(dto.getDescripcion());
        centro.setLatitud(dto.getLatitud());
        centro.setLongitud(dto.getLongitud());
        centro.setUsuario(usuario);

        CentroPrivadoUsuario centroGuardado = centroPrivadoUsuarioRepository.save(centro);
        return entityToDto(centroGuardado);
    }

    // Añadir centro privado desde lista de la app:
    public CentroPrivadoUsuarioDTO AñadirPrivadoAMisCentros(Long idBase, String username) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        CentroPrivadoBase centroBase = centroPrivadoBaseRepository.findById(idBase)
                .orElseThrow(() -> new RecursoNoEncontradoException("Centro base no encontrado en la lista."));

        CentroPrivadoUsuario centro = new CentroPrivadoUsuario();
        centro.setNombre(centroBase.getNombre());
        centro.setDireccion(centroBase.getDireccion());
        centro.setTelefono(centroBase.getTelefono());
        centro.setHorario(centroBase.getHorario());
        centro.setPrecioMensual(centroBase.getPrecioMensual());
        centro.setDescripcion(centroBase.getDescripcion());
        centro.setLatitud(centroBase.getLatitud());
        centro.setLongitud(centroBase.getLongitud());
        centro.setUsuario(usuario);

        CentroPrivadoUsuario centroGuardado = centroPrivadoUsuarioRepository.save(centro);
        return entityToDto(centroGuardado);
    }

    //Detalle CentroPrivado:
    public CentroPrivadoUsuarioDTO verDetalle(Long id,String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        CentroPrivadoUsuario centro = centroPrivadoUsuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Centro con id: " + id + " No encontrado."));

        if (!centro.getUsuario().getId().equals(usuario.getId())) {
            throw new OperacionNoPermitida("No tienes permiso para ver este centro.");
        }
        return entityToDto(centro);
    }

    //Borrar Centro de MisCentrosGuardados:
    public void borrarDeMisCentrosGuardados(Long id, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        CentroPrivadoUsuario centro = centroPrivadoUsuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Centro con id: " + id + " No encontrado."));

        if (!centro.getUsuario().getId().equals(usuario.getId())) {
            throw new OperacionNoPermitida("No tienes permiso para borrar este centro.");
        }
        centroPrivadoUsuarioRepository.delete(centro);
    }

    //Mappers:
    public CentroPrivadoUsuario dtoToEntity(CentroPrivadoUsuarioDTO dto) {
        CentroPrivadoUsuario centro = new CentroPrivadoUsuario();
        centro.setId(dto.getId());
        centro.setNombre(dto.getNombre());
        centro.setDireccion(dto.getDireccion());
        centro.setTelefono(dto.getTelefono());
        centro.setHorario(dto.getHorario());
        centro.setPrecioMensual(dto.getPrecioMensual());
        centro.setDescripcion(dto.getDescripcion());
        centro.setLatitud(dto.getLatitud());
        centro.setLongitud(dto.getLongitud());
        return centro;
    }

    public CentroPrivadoUsuarioDTO entityToDto(CentroPrivadoUsuario centro) {
        CentroPrivadoUsuarioDTO dto = new CentroPrivadoUsuarioDTO();
        dto.setId(centro.getId());
        dto.setNombre(centro.getNombre());
        dto.setDireccion(centro.getDireccion());
        dto.setTelefono(centro.getTelefono());
        dto.setHorario(centro.getHorario());
        dto.setPrecioMensual(centro.getPrecioMensual());
        dto.setDescripcion(centro.getDescripcion());
        dto.setLatitud(centro.getLatitud());
        dto.setLongitud(centro.getLongitud());
        return dto;
    }

}
