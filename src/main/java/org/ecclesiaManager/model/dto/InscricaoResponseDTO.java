package org.ecclesiaManager.model.dto;

import org.ecclesiaManager.model.Evento;
import org.ecclesiaManager.model.Inscricao;
import org.ecclesiaManager.model.Ministerio;

public record InscricaoResponseDTO(
        Long id,
        String nome,
        String email,
        String telefone,
        String numeroInscricao,
        String dataInscricao,
        Evento evento,
        String cpf,
        String tipo_pagamento,
        String status

) {

    public InscricaoResponseDTO(Inscricao inscricao) {
        this(inscricao.getId(),
                inscricao.getNome(),
                inscricao.getEmail(),
                inscricao.getTelefone(),
                inscricao.getNumeroInscricao(),
                inscricao.getDataInscricao().toString(),
                inscricao.getEvento(),
                inscricao.getCpf(),
                inscricao.getTipoPagamento(),
                inscricao.getStatus()
        );

    }

}
