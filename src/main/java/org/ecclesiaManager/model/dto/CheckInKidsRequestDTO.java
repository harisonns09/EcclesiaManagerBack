package org.ecclesiaManager.model.dto;

import jakarta.validation.constraints.NotNull;

public record CheckInKidsRequestDTO(
        @NotNull(message = "Selecione a criança")
        String nomeCrianca,
        @NotNull(message = "Selecione o responsável")
        String nomeResponsavel,
        String observacoes,
        Long igrejaId
) {
}
