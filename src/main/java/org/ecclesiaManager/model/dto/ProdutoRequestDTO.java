package org.ecclesiaManager.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProdutoRequestDTO(

        @NotBlank(message = "O nome do produto não pode ser vazio ou nulo.")
        String nome,

        String descricao,

        @NotNull(message = "O preço não pode ser nulo.")
        @PositiveOrZero(message = "O preço deve ser um valor positivo ou zero.")
        BigDecimal preco,

        @NotNull(message = "O estoque não pode ser nulo.")
        @PositiveOrZero(message = "O estoque deve ser um valor positivo ou zero.")
        Integer estoque,

        String imageUrl
) {}