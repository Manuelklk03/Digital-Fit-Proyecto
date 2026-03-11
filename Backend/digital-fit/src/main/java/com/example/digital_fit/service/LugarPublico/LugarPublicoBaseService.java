package com.example.digital_fit.service.LugarPublico;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.digital_fit.dto.LugarPublico.LugarPublicoBaseDTO;
import com.example.digital_fit.exception.RecursoNoEncontradoException;
import com.example.digital_fit.model.Enums.TipoLugarPublico;
import com.example.digital_fit.model.LugarPublico.LugarPublicoBase;
import com.example.digital_fit.repository.LugarPublico.LugarPublicoBaseRepository;

@Service
public class LugarPublicoBaseService {

    @Autowired
    private LugarPublicoBaseRepository lugarPublicoBaseRepository;

    private static final Logger log = LoggerFactory.getLogger(LugarPublicoBaseService.class);

    public List<LugarPublicoBaseDTO> listarOFiltrar(String nombre, String direccion, TipoLugarPublico tipo) {

        log.debug("Listando lugares publicos de la app");

        List<LugarPublicoBase> lugares = new ArrayList<>();

        if (nombre != null) {
            lugares = lugarPublicoBaseRepository.findByNombreContainingIgnoreCase(nombre);
        } else if (direccion != null) {
            lugares = lugarPublicoBaseRepository.findByDireccionContainingIgnoreCase(direccion);
        } else if (tipo != null) {
            lugares = lugarPublicoBaseRepository.findByTipo(tipo);
        } else {
            lugares = lugarPublicoBaseRepository.findAll();
        }

        List<LugarPublicoBaseDTO> lugaresDTO = new ArrayList<>();

        for (LugarPublicoBase lugar : lugares) {
            lugaresDTO.add(entityToDto(lugar));
        }

        return lugaresDTO;
    }

    // Ver detalles:
    public LugarPublicoBaseDTO buscarPorId(Long id) {

        log.debug("Buscando lugar con id: {}", id);

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
