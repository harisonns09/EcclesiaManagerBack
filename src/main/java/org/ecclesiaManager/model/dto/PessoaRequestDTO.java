package org.ecclesiaManager.model.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record PessoaRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        String email,
        String telefone,
        LocalDate dataNascimento,

        String ministerio,
        String status,

        String genero,
        String estadoCivil,
        String cep,
        String endereco,
        String numero,
        String bairro,
        String cidade,
        String complemento,
        String estado,
        LocalDate dataBatismo,

        Long igrejaId,

        String observacao
) {}