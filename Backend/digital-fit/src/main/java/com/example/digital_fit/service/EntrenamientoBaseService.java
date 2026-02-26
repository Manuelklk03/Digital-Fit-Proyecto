package com.example.digital_fit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.digital_fit.dto.EntrenamientoBaseDTO;
import com.example.digital_fit.exception.RecursoNoEncontradoException;
import com.example.digital_fit.model.EntrenamientoBase;
import com.example.digital_fit.repository.EntrenamientoBaseRepository;

@Service
public class EntrenamientoBaseService {

    @Autowired
    private EntrenamientoBaseRepository entrenamientoBaseRepository;

    public List<EntrenamientoBaseDTO> listarTodos() {
        List<EntrenamientoBase> entrenamientos = entrenamientoBaseRepository.findAll();

        List<EntrenamientoBaseDTO> dtos = new ArrayList<>();
        for (EntrenamientoBase entrenamiento : entrenamientos) {
            dtos.add(entityToDto(entrenamiento));
        }
        return dtos;

    }

    public EntrenamientoBaseDTO obtenerPorId(Long id) {
        EntrenamientoBase entrenamientoBase = entrenamientoBaseRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Entrenamiento base no encontrado con ID: " + id));
                
        return entityToDto(entrenamientoBase);
    }

    public EntrenamientoBaseDTO entityToDto(EntrenamientoBase entrenamientoBase) {
        EntrenamientoBaseDTO dto = new EntrenamientoBaseDTO();
        dto.setId(entrenamientoBase.getId());
        dto.setNombre(entrenamientoBase.getNombre());
        dto.setDescripcion(entrenamientoBase.getDescripcion());
        return dto;
    }

    public EntrenamientoBase dtoToEntity(EntrenamientoBaseDTO dto) {
        EntrenamientoBase entrenamientoBase = new EntrenamientoBase();
        entrenamientoBase.setId(dto.getId());
        entrenamientoBase.setNombre(dto.getNombre());
        entrenamientoBase.setDescripcion(dto.getDescripcion());
        return entrenamientoBase;
    }
}
