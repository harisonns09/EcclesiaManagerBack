package org.ecclesiaManager.model.dto;

import org.ecclesiaManager.model.Transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoResponseDTO(
        Long id,
        Long igrejaId,
        String descricao,
        BigDecimal valor,
        LocalDateTime dataRegistro,
        String tipo,
        String categoria,
        Long eventoId
) {

    public TransacaoResponseDTO(Transacao transacao){
        this(
            transacao.getId(),
            transacao.getIgreja().getId(),
            transacao.getDescricao(),
            transacao.getValor(),
            transacao.getDataRegistro(),
            transacao.getTipo(),
            transacao.getCategoria(),
            transacao.getEvento().getId()
        );
    }
}
