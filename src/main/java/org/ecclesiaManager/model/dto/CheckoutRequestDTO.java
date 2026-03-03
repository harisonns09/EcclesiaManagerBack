package org.ecclesiaManager.model.dto;

import java.math.BigDecimal;

public record CheckoutRequestDTO(
        String nome,
        String email,
        String telefone,
        String cpf,
        String numeroInscricao,
        BigDecimal amount
) {}