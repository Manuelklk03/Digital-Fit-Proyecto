package com.example.digital_fit.service.LugarPublico;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.digital_fit.dto.LugarPublico.CrearLugarPublicoDTO;
import com.example.digital_fit.dto.LugarPublico.LugarPublicoUsuarioDTO;
import com.example.digital_fit.exception.OperacionNoPermitida;
import com.example.digital_fit.exception.RecursoNoEncontradoException;
import com.example.digital_fit.model.Auth.Usuario;
import com.example.digital_fit.model.LugarPublico.LugarPublicoBase;
import com.example.digital_fit.model.LugarPublico.LugarPublicoUsuario;
import com.example.digital_fit.repository.Auth.UsuarioRepository;
import com.example.digital_fit.repository.LugarPublico.LugarPublicoBaseRepository;
import com.example.digital_fit.repository.LugarPublico.LugarPublicoUsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class LugarPublicoUsuarioService {

    @Autowired
    private LugarPublicoUsuarioRepository lugarPublicoUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LugarPublicoBaseRepository lugarPublicoBaseRepository;

    // Listar mis lugares:
    public List<LugarPublicoUsuarioDTO> listarLugares(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        List<LugarPublicoUsuario> lugares = lugarPublicoUsuarioRepository.findByUsuario(usuario);

        List<LugarPublicoUsuarioDTO> dtos = new ArrayList<>();

        for (LugarPublicoUsuario lugar : lugares) {
            dtos.add(entityToDto(lugar));
        }
        return dtos;
    }

    // Ver detalles de un lugar:
    public LugarPublicoUsuarioDTO verDetalle(Long id, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        LugarPublicoUsuario lugar = lugarPublicoUsuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Lugar con id: " + id + " No encontrado."));

        if (!lugar.getUsuario().getId().equals(usuario.getId())) {
            throw new OperacionNoPermitida("No tienes permiso para ver este lugar.");
        }
        return entityToDto(lugar);
    }

    // Borrar un lugar:
    public void borrarLugar(Long id) {
        LugarPublicoUsuario lugar = lugarPublicoUsuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Lugar con id: " + id + " No encontrado."));

        lugarPublicoUsuarioRepository.delete(lugar);
    }

    // Guardar desde base:
    @Transactional
    public LugarPublicoUsuarioDTO guardarDesdeBase(Long lugarId, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        LugarPublicoBase lugarBase = lugarPublicoBaseRepository.findById(lugarId)
                .orElseThrow(
                        () -> new RecursoNoEncontradoException("Lugar base con id: " + lugarId + " No encontrado."));

        LugarPublicoUsuario lugar = new LugarPublicoUsuario();

        lugar.setUsuario(usuario);
        lugar.setNombre(lugarBase.getNombre());
        lugar.setDireccion(lugarBase.getDireccion());
        lugar.setDescripcion(lugarBase.getDescripcion());
        lugar.setTelefono(lugarBase.getTelefono());
        lugar.setHorario(lugarBase.getHorario());
        lugar.setLatitud(lugarBase.getLatitud());
        lugar.setLongitud(lugarBase.getLongitud());
        lugar.setTipo(lugarBase.getTipo());

        LugarPublicoUsuario lugarGuardado = lugarPublicoUsuarioRepository.save(lugar);

        return entityToDto(lugarGuardado);
    }

    // Guardar desde maps:
    @Transactional
    public LugarPublicoUsuarioDTO guardarDesdeMaps(CrearLugarPublicoDTO dto, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        LugarPublicoUsuario lugar = new LugarPublicoUsuario();

        lugar.setUsuario(usuario);
        lugar.setNombre(dto.getNombre());
        lugar.setDireccion(dto.getDireccion());
        lugar.setDescripcion(dto.getDescripcion());
        lugar.setTelefono(dto.getTelefono());
        lugar.setHorario(dto.getHorario());
        lugar.setLatitud(dto.getLatitud());
        lugar.setLongitud(dto.getLongitud());
        lugar.setTipo(dto.getTipo());

        LugarPublicoUsuario lugarGuardado = lugarPublicoUsuarioRepository.save(lugar);

        return entityToDto(lugarGuardado);
    }

    // MAPPERS:
    public LugarPublicoUsuario dtoToEntity(LugarPublicoUsuarioDTO dto) {
        LugarPublicoUsuario lugar = new LugarPublicoUsuario();

        lugar.setId(dto.getId());
        lugar.setNombre(dto.getNombre());
        lugar.setDireccion(dto.getDireccion());
        lugar.setDescripcion(dto.getDescripcion());
        lugar.setTelefono(dto.getTelefono());
        lugar.setHorario(dto.getHorario());
        lugar.setLatitud(dto.getLatitud());
        lugar.setLongitud(dto.getLongitud());
        lugar.setTipo(dto.getTipo());

        return lugar;
    }

    public LugarPublicoUsuarioDTO entityToDto(LugarPublicoUsuario lugar) {
        LugarPublicoUsuarioDTO dto = new LugarPublicoUsuarioDTO();

        dto.setId(lugar.getId());
        dto.setNombre(lugar.getNombre());
        dto.setDireccion(lugar.getDireccion());
        dto.setDescripcion(lugar.getDescripcion());
        dto.setTelefono(lugar.getTelefono());
        dto.setHorario(lugar.getHorario());
        dto.setLatitud(lugar.getLatitud());
        dto.setLongitud(lugar.getLongitud());
        dto.setTipo(lugar.getTipo());

        return dto;
    }
}
