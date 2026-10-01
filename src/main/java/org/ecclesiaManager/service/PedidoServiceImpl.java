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

    @Inject
    ProdutoRepository produtoRepository;

    @Override
    @Transactional
    public Pedido processarVenda(Long igrejaId, PedidoRequestDTO dto) {
        // 1. Busca a Igreja
        Igreja igreja = igrejaRepository.findByIdOptional(igrejaId)
                .orElseThrow(() -> new RuntimeException("Igreja não encontrada"));

        // 2. Busca o Produto usando o ID que veio do Frontend
        Produto produto = produtoRepository.findByIdOptional(dto.produtoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // 3. Monta os dados base do Pedido
        Pedido pedido = new Pedido();
        pedido.setIgreja(igreja);
        pedido.setComprador(dto.nomeComprador());
        pedido.setStatusPagamento(StatusPagamento.PENDENTE);
        pedido.setValorTotal(dto.amount());
        pedido.setTransacaoId(dto.codigoCompra());
        pedido.setDescricao(dto.description());

        // 4. Cria o Item do Pedido
        ItemPedido item = new ItemPedido();
        item.setPedido(pedido); // Vincula o item ao pedido
        item.setProduto(produto); // Vincula o produto ao item
        item.setQuantidade(dto.quantidade());
        item.setPrecoUnitario(produto.getPreco()); // Guarda o preço que o produto custava hoje

        // 5. Adiciona o item na lista do pedido
        pedido.getItens().add(item);

        /*
         * Observação: O estoque NÃO é subtraído aqui.
         * Como o status do pagamento é PENDENTE, o estoque só será reduzido
         * lá no webhook, quando o pagamento for aprovado (como conversamos antes).
         */

        // 6. O pedido é salvo. Graças ao cascade=ALL, o ItemPedido é salvo automaticamente junto com ele
        pedidoRepository.persist(pedido);

        return pedido;
    }


    @Override
    public List<Pedido> listar(Long igrejaId) {
        return pedidoRepository.findAllByIgrejaId(igrejaId) ;
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
        String nrTransacao = payload.orderNsu();
        log.info("Webhook InfinitePay recebido. NSU/Transacao: {}", nrTransacao);

        if (nrTransacao == null) {
            log.error("Webhook recebido sem OrderNSU (ID Transacao). Payload: {}", payload);
            return;
        }

        Pedido pedido = pedidoRepository.findById(Long.valueOf(nrTransacao));
        if (pedido == null) {
            log.error("Pedido não encontrado para o número: {}", nrTransacao);
            return;
        }

        if (!"PAGO".equals(pedido.getStatusPagamento())) {
            pedido.setStatusPagamento(StatusPagamento.PAGO);
            pedido.setDataPagamento(java.time.LocalDateTime.now());
            pedido.setComprovante(payload.receiptUrl());

            pedidoRepository.persist(pedido);

            log.info("Pagamento confirmado com sucesso via Webhook para transação: #{}", nrTransacao);
        } else {
            log.warn("Webhook ignorado: Transação #{} já consta como PAGO.", nrTransacao);
        }

        for (ItemPedido item : pedido.getItens()) {
            Produto produto = item.getProduto();

            int novoEstoque = produto.getEstoque() - item.getQuantidade();

            // Evita que o estoque fique negativo caso ocorram compras simultâneas
            if (novoEstoque < 0) {
                novoEstoque = 0;
                // Dica: Aqui você poderia enviar um alerta interno de que vendeu mais do que tinha
            }

            produto.setEstoque(novoEstoque);

            // Salva o produto atualizado no banco
            produtoRepository.persist(produto); // Use .save(produto) se estiver no Spring Data
        }
    }

}