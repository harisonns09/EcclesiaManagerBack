package org.ecclesiaManager.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.ecclesiaManager.enums.StatusPagamento;
import org.ecclesiaManager.model.*;
import org.ecclesiaManager.model.dto.CheckoutResponseDTO;
import org.ecclesiaManager.model.dto.PedidoRequestDTO;
import org.ecclesiaManager.repository.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PedidoServiceImpl implements IPedidoService {

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
}