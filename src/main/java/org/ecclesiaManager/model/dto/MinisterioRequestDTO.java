package org.ecclesiaManager.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record MinisterioRequestDTO(
        Long id,
        @NotBlank(message = "O nome é obrigatório)")
        String nome,
        Long igrejaId,
        @NotBlank(message = "Líder obrigatorio")
        String liderResponsavel
){

}
