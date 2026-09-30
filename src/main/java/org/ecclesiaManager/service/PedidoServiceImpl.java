package org.ecclesiaManager.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.ecclesiaManager.enums.StatusPagamento;
import org.ecclesiaManager.model.*;
import org.ecclesiaManager.model.dto.PedidoRequestDTO;
import org.ecclesiaManager.model.dto.infinitepay.InfinitePayWebhookDTO;
import org.ecclesiaManager.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
public class PedidoServiceImpl implements IPedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoServiceImpl.class);

    @Inject
    PedidoRepository pedidoRepository;

    @Inject
    IgrejaRepository igrejaRepository;

    @Override
    @Transactional
    public Pedido processarVenda(Long igrejaId, PedidoRequestDTO dto) {
        Igreja igreja = igrejaRepository.findByIdOptional(igrejaId)
                .orElseThrow(() -> new RuntimeException("Igreja não encontrada"));

        Pedido pedido = new Pedido();
        pedido.setIgreja(igreja);
        pedido.setComprador(dto.nomeComprador());
        pedido.setStatusPagamento(StatusPagamento.PENDENTE);
        pedido.setValorTotal(dto.amount());
        pedido.setTransacaoId(dto.codigoCompra());

        // O pedido é salvo aqui, mas a transação ainda não foi commitada
        pedidoRepository.persist(pedido);


        // A transação será commitada aqui, salvando o pedido no banco.
        return pedido;
    }


    @Override
    public List<Pedido> listar(Long igrejaId) {
        return pedidoRepository.findAllByIgrejaId(igrejaId);
    }

    @Override
    public Pedido buscarPorId(Long igrejaId, Long id) {
        Pedido pedido = pedidoRepository.findByIdOptional(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (!pedido.getIgreja().getId().equals(igrejaId)) {
            throw new RuntimeException("Pedido não pertence a esta igreja");
        }

        return pedido;
    }

    @Override
    @Transactional
    public void processarPagamentoWebhook(InfinitePayWebhookDTO payload) {
        String nrTransacao = payload.transactionNsu();
        log.info("Webhook InfinitePay recebido. NSU/Transacao: {}", nrTransacao);

        if (nrTransacao == null) {
            log.error("Webhook recebido sem OrderNSU (ID Transacao). Payload: {}", payload);
            return;
        }

        Pedido pedido = pedidoRepository.findByNumero_Transacao(nrTransacao);
        if (pedido == null) {
            log.error("Inscrição não encontrada para o número: {}", nrTransacao);
            return;
        }

        if (pedido.getStatusPagamento() != StatusPagamento.PAGO) {
            pedido.setStatusPagamento(StatusPagamento.PAGO);
            pedido.setDataPagamento(java.time.LocalDateTime.now());
            pedido.setComprovante(payload.receiptUrl());

            log.info("Pagamento confirmado com sucesso via Webhook para transação: #{}", nrTransacao);
        } else {
            log.warn("Webhook ignorado: Transação #{} já consta como PAGO.", nrTransacao);
        }
    }

}