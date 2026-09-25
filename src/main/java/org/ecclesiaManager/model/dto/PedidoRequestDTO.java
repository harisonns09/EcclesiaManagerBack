package org.ecclesiaManager.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record PedidoRequestDTO(
        String nomeComprador,
        String emailComprador,
        String telefoneComprador,
        String cpfComprador, // O Java aceita nulo sem problemas se o front não enviar
        Long produtoId,
        Integer quantidade,
        BigDecimal amount
) {}
