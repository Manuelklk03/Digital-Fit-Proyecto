package com.example.digital_fit.service.LugarPublico;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.digital_fit.dto.LugarPublico.LugarPublicoBaseDTO;
import com.example.digital_fit.exception.RecursoNoEncontradoException;
import com.example.digital_fit.model.LugarPublico.LugarPublicoBase;
import com.example.digital_fit.repository.LugarPublico.LugarPublicoBaseRepository;

@Service
public class LugarPublicoBaseService {

    @Autowired
    private LugarPublicoBaseRepository lugarPublicoBaseRepository;

    public List<LugarPublicoBaseDTO> listarOFiltrar() {

    }

    // Ver detalles:
    public LugarPublicoBaseDTO buscarPorId(Long id) {
        LugarPublicoBase lugar = lugarPublicoBaseRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Lugar con id: " + id + " No encontrado."));

        return entityToDto(lugar);
    }

    // MAPPERS:

    public LugarPublicoBase dtoToEntity(LugarPublicoBaseDTO dto) {
        LugarPublicoBase lugar = new LugarPublicoBase();
        lugar.setId(dto.getId());
        lugar.setNombre(dto.getNombre());
        lugar.setDireccion(dto.getDireccion());
        lugar.setTelefono(dto.getTelefono());
        lugar.setHorario(dto.getHorario());
        lugar.setDescripcion(dto.getDescripcion());
        lugar.setLatitud(dto.getLatitud());
        lugar.setLongitud(dto.getLongitud());
        lugar.setTipo(dto.getTipo());
        return lugar;
    }

    public LugarPublicoBaseDTO entityToDto(LugarPublicoBase lugar) {
        LugarPublicoBaseDTO dto = new LugarPublicoBaseDTO();
        dto.setId(lugar.getId());
        dto.setNombre(lugar.getNombre());
        dto.setDireccion(lugar.getDireccion());
        dto.setTelefono(lugar.getTelefono());
        dto.setHorario(lugar.getHorario());
        dto.setDescripcion(lugar.getDescripcion());
        dto.setLatitud(lugar.getLatitud());
        dto.setLongitud(lugar.getLongitud());
        dto.setTipo(lugar.getTipo());
        return dto;
    }
}
