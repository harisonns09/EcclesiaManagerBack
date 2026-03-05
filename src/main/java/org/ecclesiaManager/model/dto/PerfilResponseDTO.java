package org.ecclesiaManager.model.dto;

import org.ecclesiaManager.model.Perfil;

import java.util.List;
import java.util.stream.Collectors;

public record PerfilResponseDTO(
        Long id,
        String nome,
        List<String> permissoes
){
    public PerfilResponseDTO(Perfil perfil) {
        this(
                perfil.getId(),
                perfil.getNome(),
                perfil.getPermissoes() != null
                        ? perfil.getPermissoes().stream()
                        .map(permissao -> permissao.getNome())
                        .collect(Collectors.toList())
                        : List.of()
        );
    }
}
