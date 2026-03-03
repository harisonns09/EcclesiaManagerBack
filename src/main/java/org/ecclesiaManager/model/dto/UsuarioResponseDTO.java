package org.ecclesiaManager.model.dto;

import org.ecclesiaManager.model.Usuario;

public record UsuarioResponseDTO(
        Long id,
        String user,
        String role,
        Long igreja
) {

    public UsuarioResponseDTO(Usuario usuario){
        this(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole() != null ? usuario.getRole().name() : "SEM_PERMISSAO",
                usuario.getIgreja().getId()
        );
    }
}
