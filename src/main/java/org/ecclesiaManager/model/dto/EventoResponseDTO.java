package org.ecclesiaManager.model.dto;

import org.ecclesiaManager.model.Evento;
import org.ecclesiaManager.model.Inscricao;
import org.ecclesiaManager.model.Pessoa;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record EventoResponseDTO(
        Long id,
        String nomeEvento,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dataEvento,
        @JsonFormat(pattern = "HH:mm")
        LocalTime horario,
        String local,
        BigDecimal preco,
        BigDecimal precoPromocional,
        String descricao,
        String ministerioResponsavel,
        List<InscritoResumoDTO> inscricoes
) {

    public EventoResponseDTO(Evento evento) {
        this(evento.getId(),
                evento.getNomeEvento(),
                evento.getDataEvento(),
                evento.getHorario(),
                evento.getLocal(),
                evento.getPreco(),
                evento.getPrecoPromocional(),
                evento.getDescricao(),
                evento.getMinisterioResponsavel(),
                evento.getInscricoes() != null ?
                        evento.getInscricoes().stream()
                                .map(i -> new InscritoResumoDTO(i.getNome(), i.getEmail(), i.getTelefone(), i.getNumeroInscricao(), i.getStatus(), i.getDataInscricao(), i.getTipoPagamento(), i.getTipoValorPagamento(), i.getValorPago()))
                                .toList()
                        : Collections.emptyList()
        );
    }

}
