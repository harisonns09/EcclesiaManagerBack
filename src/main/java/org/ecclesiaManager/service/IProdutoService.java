package org.ecclesiaManager.service;

import org.ecclesiaManager.model.Produto;
import org.ecclesiaManager.model.dto.ProdutoRequestDTO;
import java.util.List;

public interface IProdutoService {
    Produto criar(Long igrejaId, ProdutoRequestDTO dto);
    Produto atualizar(Long igrejaId, Long id, ProdutoRequestDTO dto);
    List<Produto> listar(Long igrejaId);
    Produto buscarPorId(Long igrejaId, Long id);
}