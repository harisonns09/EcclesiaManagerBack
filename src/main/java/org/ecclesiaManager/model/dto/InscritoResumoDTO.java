package org.ecclesiaManager.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InscritoResumoDTO (
        String nome,
        String email,
        String telefone,
        String numero_inscricao,
        String status,
        LocalDateTime data_inscricao,
        String tipoPagamento,
        String tipoValorPagamento,
        BigDecimal valorPago
){
}
