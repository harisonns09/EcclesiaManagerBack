package org.ecclesiaManager.model.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record PerfilRequestDTO(

        @NotBlank(message = "O nome do perfil é obrigatório")
        String nome,

        // Lista com os nomes das permissões vinculadas a este perfil
        List<String> permissoes
) {
}