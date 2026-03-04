package org.ecclesiaManager.model.dto;

import jakarta.validation.constraints.NotNull;

public record CheckInKidsRequestDTO(
        @NotNull(message = "Selecione a criança")
        String nomeCrianca,
        @NotNull(message = "Selecione o responsável")
        String nomeResponsavel,
        String telefoneResponsavel,
        String observacoes,
        Long igrejaId
) {
}
