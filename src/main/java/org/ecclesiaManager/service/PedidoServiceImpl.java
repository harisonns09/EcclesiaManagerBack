package org.ecclesiaManager.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.ecclesiaManager.enums.StatusPagamento;
import org.ecclesiaManager.model.*;
import org.ecclesiaManager.model.dto.PedidoRequestDTO;
import org.ecclesiaManager.repository.*;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class PedidoServiceImpl implements IPedidoService {

    @Inject
    PedidoRepository pedidoRepository;

    @Inject
    ProdutoRepository produtoRepository;

    @Inject
    PessoaRepository pessoaRepository;

    @Inject
    IgrejaRepository igrejaRepository;

    // Injetar o seu serviço existente
    @Inject
    InfinitePayService infinitePayService;

    @Override
    @Transactional
    public Pedido processarVenda(Long igrejaId, PedidoRequestDTO dto) {
        Igreja igreja = igrejaRepository.findByIdOptional(igrejaId)
                .orElseThrow(() -> new RuntimeException("Igreja não encontrada"));

        Pessoa comprador = pessoaRepository.findByIdOptional(dto.pessoaId())
                .orElseThrow(() -> new RuntimeException("Comprador não encontrado"));

        Pedido pedido = new Pedido();
        pedido.setIgreja(igreja);
        pedido.setComprador(comprador);
        pedido.setStatusPagamento(StatusPagamento.PENDENTE);

        BigDecimal total = BigDecimal.ZERO;

        for (var itemDto : dto.itens()) {
            Produto produto = produtoRepository.findByIdOptional(itemDto.produtoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            if (produto.getEstoque() < itemDto.quantidade()) {
                throw new RuntimeException("Estoque insuficiente para: " + produto.getNome());
            }

            produto.setEstoque(produto.getEstoque() - itemDto.quantidade());

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(itemDto.quantidade());
            item.setPrecoUnitario(produto.getPreco());

            pedido.getItens().add(item);
            total = total.add(produto.getPreco().multiply(BigDecimal.valueOf(itemDto.quantidade())));
        }

        pedido.setValorTotal(total);

        // 1. Guardar o pedido para gerar o ID que enviaremos para a InfinitePay
        pedidoRepository.persist(pedido);

        // 2. Integração com a InfinitePay
        if (total.compareTo(BigDecimal.ZERO) > 0) {
            try {
                // Adapte o nome do método caso seja diferente no seu InfinitePayService
                String linkPagamento = infinitePayService.gerarLinkProdutoCheckout(pedido);
                pedido.setLinkPagamento(linkPagamento);

                // Atualiza o pedido com o link
                pedidoRepository.persist(pedido);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao gerar link de pagamento: " + e.getMessage());
            }
        } else {
            pedido.setStatusPagamento(StatusPagamento.PAGO);
        }

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


}