package org.ecclesiaManager.service;

import org.ecclesiaManager.model.dto.EventoRequestDTO;
import org.ecclesiaManager.model.dto.EventoResponseDTO;
import org.ecclesiaManager.model.dto.InscricaoRequestDTO;
import org.ecclesiaManager.model.dto.InscricaoResponseDTO;
import org.ecclesiaManager.model.dto.infinitepay.InfinitePayWebhookDTO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface IEventoService {

    List<EventoResponseDTO> findByIgrejaId(Long idIgreja);

    Optional<EventoResponseDTO> findById(Long eventoId);

    EventoResponseDTO addEvento(Long idIgreja, EventoRequestDTO dto);

    void deleteById(Long idIgreja, Long eventoId);

    EventoResponseDTO update(Long idIgreja, Long eventoId, EventoRequestDTO dto);

    InscricaoResponseDTO realizarInscricao(Long eventoId, @Valid InscricaoRequestDTO dto);

    void processarPagamentoWebhook(InfinitePayWebhookDTO payload);

    void atualizarMetodoPagamento(String idInscricao, String tipoPagamento);
}