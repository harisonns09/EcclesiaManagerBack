package org.ecclesiaManager.model.dto;

import org.ecclesiaManager.enums.UserRole;

import java.util.List;

public record UsuarioRequestDTO(
        Long id,
        String user,
        String password,
        String perfil,              // Mudou de UserRole (Enum) para String
        List<String> permissions,
        Long igrejaId
) {
}
