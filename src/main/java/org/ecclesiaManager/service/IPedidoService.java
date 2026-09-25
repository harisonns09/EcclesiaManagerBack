package org.ecclesiaManager.service;

import org.ecclesiaManager.model.Pedido;
import org.ecclesiaManager.model.dto.PedidoRequestDTO;

import java.util.List;

public interface IPedidoService {
    Pedido processarVenda(Long igrejaId, PedidoRequestDTO dto);
    List<Pedido> listar(Long igrejaId);
    Pedido buscarPorId(Long igrejaId, Long id);
}