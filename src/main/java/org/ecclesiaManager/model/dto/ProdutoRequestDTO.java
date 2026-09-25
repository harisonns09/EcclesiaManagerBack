package org.ecclesiaManager.model.dto;

import java.math.BigDecimal;

public record ProdutoRequestDTO(
        String nome,
        String descricao,
        BigDecimal preco,
        Integer estoque,
        String imageUrl,
        Boolean ativo
) {}