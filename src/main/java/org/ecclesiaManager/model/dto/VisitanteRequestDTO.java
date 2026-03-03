package org.ecclesiaManager.model.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record VisitanteRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String nome,
        String telefone,
        LocalDate dataNascimento,
        String status,
        Long igrejaId,
        String observacao
) {}