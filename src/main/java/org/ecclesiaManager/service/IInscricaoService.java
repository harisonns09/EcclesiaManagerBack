package org.ecclesiaManager.service;

import org.ecclesiaManager.model.dto.InscricaoResponseDTO;
import java.util.List;

public interface IInscricaoService {

    List<InscricaoResponseDTO> buscarInscricoesPorCpf(String cpf);

    void confirmarPagamento(String idInscricao, String tipoValor);
}