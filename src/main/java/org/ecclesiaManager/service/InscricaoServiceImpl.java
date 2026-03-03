package org.ecclesiaManager.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.ecclesiaManager.enums.FormaPagamento;
import org.ecclesiaManager.enums.StatusPagamento;
import org.ecclesiaManager.enums.TipoValorPagamento;
import org.ecclesiaManager.model.Inscricao;
import org.ecclesiaManager.model.dto.InscricaoResponseDTO;
import org.ecclesiaManager.repository.InscricaoRepository;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class InscricaoServiceImpl implements IInscricaoService {

    @Inject
    InscricaoRepository inscricaoRepository;

    @Override
    public List<InscricaoResponseDTO> buscarInscricoesPorCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("O CPF é obrigatório para a consulta.");
        }

        String cpfLimpo = cpf.replaceAll("\\D", "");

        List<Inscricao> inscricoes = inscricaoRepository.findAllByCpf(cpfLimpo);

        return inscricoes.stream()
                .map(InscricaoResponseDTO::new)
                .toList();
    }

    @Override
    @Transactional
    public void confirmarPagamento(String idInscricao, String tipoValor) {
        Inscricao inscricao = inscricaoRepository.findByNumero_Inscricao(idInscricao);

        if (inscricao != null && inscricao.getStatus().equals(StatusPagamento.PENDENTE.getStatusPagamento())) {
            inscricao.setStatus(StatusPagamento.PAGO.getStatusPagamento());
            inscricao.setDataPagamento(LocalDateTime.now());
            inscricao.setTipoPagamento(FormaPagamento.DINHEIRO.getFormaPagamento());

            if ("INTEGRAL".equals(tipoValor)) {
                inscricao.setTipoValorPagamento(TipoValorPagamento.INTEGRAL.getTipoValorPagamento());
                inscricao.setValorPago(inscricao.getEvento().getPreco());
            } else if ("PROMOCIONAL".equals(tipoValor)) {
                inscricao.setTipoValorPagamento(TipoValorPagamento.PROMOCIONAL.getTipoValorPagamento());
                inscricao.setValorPago(inscricao.getEvento().getPrecoPromocional());
            }
        }
    }
}