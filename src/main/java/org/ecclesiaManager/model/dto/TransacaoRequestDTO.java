package org.ecclesiaManager.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoRequestDTO(
        @NotNull(message = "ID da igreja é obrigatório")
        Long igrejaId,

        @NotBlank(message = "Tipo da transação é obrigatório (ENTRADA/SAIDA)")
        String tipo,

        @NotNull(message = "Valor é obrigatório")
        @Positive(message = "Valor deve ser positivo")
        BigDecimal valor,

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @NotBlank(message = "Categoria é obrigatória")
        String categoria,

        @NotNull(message = "Data é obrigatória")
        LocalDateTime dataRegistro,

        Long eventoId
) {
}
