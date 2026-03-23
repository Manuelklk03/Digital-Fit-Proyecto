package com.example.digital_fit.dto.Auth;

import com.example.digital_fit.model.Enums.Rol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioSesionDTO {

    private String username;

    private String email;
    
    private Rol rol;
}
