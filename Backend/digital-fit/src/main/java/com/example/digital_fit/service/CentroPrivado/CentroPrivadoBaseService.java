package com.example.digital_fit.service.CentroPrivado;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.digital_fit.dto.CentroPrivado.CentroPrivadoBaseDTO;
import com.example.digital_fit.dto.CentroPrivado.CrearCentroPrivadoDTO;
import com.example.digital_fit.exception.RecursoNoEncontradoException;
import com.example.digital_fit.model.CentroPrivado.CentroPrivadoBase;
import com.example.digital_fit.repository.CentroPrivado.CentroPrivadoBaseRepository;

import jakarta.transaction.Transactional;

@Service
public class CentroPrivadoBaseService {

    @Autowired
    private CentroPrivadoBaseRepository centroPrivadoBaseRepository;

    private static final Logger log = LoggerFactory.getLogger(CentroPrivadoBaseService.class);

    // Listar o filtrar
    public List<CentroPrivadoBaseDTO> listarOFiltrar(String nombre, String direccion, Double precioMensual) {

        log.debug("Listando centros privados");

        List<CentroPrivadoBase> centros = new ArrayList<>();

        if (nombre != null) {
            centros = centroPrivadoBaseRepository.findByNombreContainingIgnoreCase(nombre);
        } else if (direccion != null) {
            centros = centroPrivadoBaseRepository.findByDireccionContainingIgnoreCase(direccion);
        } else if (precioMensual != null) {
            centros = centroPrivadoBaseRepository.findByPrecioMensualLessThanEqual(precioMensual);
        } else {
            centros = centroPrivadoBaseRepository.findAll();
        }

        List<CentroPrivadoBaseDTO> centrosDTO = new ArrayList<>();

        for (CentroPrivadoBase centro : centros) {
            centrosDTO.add(entityToDto(centro));
        }

        return centrosDTO;
    }

    // Ver detalle
    public CentroPrivadoBaseDTO obtenerPorId(Long id) {

        log.debug("Buscando centro privado con id: {}", id);

        CentroPrivadoBase entidad = centroPrivadoBaseRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Centro con id: " + id + " No encontrado."));

        return entityToDto(entidad);
    }

    // Metodos panel admin:
    @Transactional
    public CentroPrivadoBaseDTO crearCentroPrivado(CrearCentroPrivadoDTO dto) {

        log.info("Creando centro privado {}", dto.getNombre());

        CentroPrivadoBase centroPrivadoBase = new CentroPrivadoBase();
        centroPrivadoBase.setNombre(dto.getNombre());
        centroPrivadoBase.setDireccion(dto.getDireccion());
        centroPrivadoBase.setTelefono(dto.getTelefono());
        centroPrivadoBase.setHorario(dto.getHorario());
        centroPrivadoBase.setPrecioMensual(dto.getPrecioMensual());
        centroPrivadoBase.setDescripcion(dto.getDescripcion());
        centroPrivadoBase.setLatitud(dto.getLatitud());
        centroPrivadoBase.setLongitud(dto.getLongitud());

        centroPrivadoBase = centroPrivadoBaseRepository.save(centroPrivadoBase);

        return entityToDto(centroPrivadoBase);
    }

    @Transactional
    public CentroPrivadoBaseDTO actualizarCentroPrivado(Long id, CrearCentroPrivadoDTO dto) {

        log.info("Actualizando centro privado con id: {}", id);

        CentroPrivadoBase centroPrivadoBase = centroPrivadoBaseRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Centro con id: " + id + " No encontrado."));

        centroPrivadoBase.setNombre(dto.getNombre());
        centroPrivadoBase.setDireccion(dto.getDireccion());
        centroPrivadoBase.setTelefono(dto.getTelefono());
        centroPrivadoBase.setHorario(dto.getHorario());
        centroPrivadoBase.setPrecioMensual(dto.getPrecioMensual());
        centroPrivadoBase.setDescripcion(dto.getDescripcion());
        centroPrivadoBase.setLatitud(dto.getLatitud());
        centroPrivadoBase.setLongitud(dto.getLongitud());

        centroPrivadoBase = centroPrivadoBaseRepository.save(centroPrivadoBase);

        log.info("Centro privado actualizado con id: {}", id);

        return entityToDto(centroPrivadoBase);
    }

    @Transactional
    public void borrarCentroPrivado(Long id) {

        log.info("Borrando centro privado con id: {}", id);

        CentroPrivadoBase entidad = centroPrivadoBaseRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Centro con id: " + id + " No encontrado."));

        centroPrivadoBaseRepository.delete(entidad);

        log.info("Centro privado con id: {} borrado", id);
    }

    // Mappers
    public CentroPrivadoBaseDTO entityToDto(CentroPrivadoBase entity) {
        CentroPrivadoBaseDTO dto = new CentroPrivadoBaseDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setDireccion(entity.getDireccion());
        dto.setTelefono(entity.getTelefono());
        dto.setHorario(entity.getHorario());
        dto.setPrecioMensual(entity.getPrecioMensual());
        dto.setDescripcion(entity.getDescripcion());
        dto.setLatitud(entity.getLatitud());
        dto.setLongitud(entity.getLongitud());
        return dto;
    }

    public CentroPrivadoBase dtoToEntity(CentroPrivadoBaseDTO dto) {
        CentroPrivadoBase entity = new CentroPrivadoBase();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setDireccion(dto.getDireccion());
        entity.setTelefono(dto.getTelefono());
        entity.setHorario(dto.getHorario());
        entity.setPrecioMensual(dto.getPrecioMensual());
        entity.setDescripcion(dto.getDescripcion());
        entity.setLatitud(dto.getLatitud());
        entity.setLongitud(dto.getLongitud());
        return entity;
    }
}
