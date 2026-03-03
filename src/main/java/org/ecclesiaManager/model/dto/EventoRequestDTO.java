package org.ecclesiaManager.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record EventoRequestDTO(
        @NotBlank(message = "O nome é obrigatório)")
        String nomeEvento,

        Long igrejaId,

        @NotNull(message = "A data do evento é obrigatória")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dataEvento,

        String descricao,

        String ministerioResponsavel,

        @JsonFormat(pattern = "HH:mm")
        LocalTime horario,

        String local,

        BigDecimal preco,
        BigDecimal precoPromocional


){

}
