package org.ecclesiaManager.model.dto;

import org.ecclesiaManager.model.Usuario;

import java.util.List;
import java.util.stream.Collectors;

public record UsuarioResponseDTO(
        Long id,
        String user,
        String perfil,
        List<String> permissions, // 🔥 NOVO: Devolve a lista para o Front-end
        Long igreja
) {

    public UsuarioResponseDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getUsername(),

                // 1. Pega o nome do Perfil dinâmico (ex: "TESOUREIRO")
                usuario.getPerfil() != null ? usuario.getPerfil().getNome() : "SEM_PERFIL",

                // 2. Transforma a lista de entidades Permissao numa lista de Strings com os nomes
                usuario.getPermissoes() != null
                        ? usuario.getPermissoes().stream()
                        .map(permissao -> permissao.getNome())
                        .collect(Collectors.toList())
                        : List.of(),

                usuario.getIgreja().getId()
        );
    }
}