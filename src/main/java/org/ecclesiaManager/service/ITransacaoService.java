package org.ecclesiaManager.service;

import org.ecclesiaManager.model.dto.TransacaoRequestDTO;
import org.ecclesiaManager.model.dto.TransacaoResponseDTO;

import java.util.List;

public interface ITransacaoService {


    List<TransacaoResponseDTO> listarTransacoes(Long igrejaId);

    List<TransacaoResponseDTO> listarTransacoesEvento(Long eventoId);

    TransacaoResponseDTO realizarTransacao(TransacaoRequestDTO dto);
}
