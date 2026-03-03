package org.ecclesiaManager.model.dto;

import org.ecclesiaManager.enums.UserRole;

public record UsuarioRequestDTO(
        Long id,
        String user,
        String password,
        UserRole role,
        Long igrejaId) {
}
