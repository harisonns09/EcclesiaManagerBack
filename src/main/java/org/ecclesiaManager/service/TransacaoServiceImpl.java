package org.ecclesiaManager.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.ecclesiaManager.infra.audit.Loggable;
import org.ecclesiaManager.model.Evento;
import org.ecclesiaManager.model.Igreja;
import org.ecclesiaManager.model.Transacao;
import org.ecclesiaManager.model.dto.TransacaoRequestDTO;
import org.ecclesiaManager.model.dto.TransacaoResponseDTO;
import org.ecclesiaManager.repository.EventoRepository;
import org.ecclesiaManager.repository.IgrejaRepository;
import org.ecclesiaManager.repository.TransacaoRepository;

import java.util.List;

@ApplicationScoped
public class TransacaoServiceImpl implements  ITransacaoService{


    @Inject
    TransacaoRepository transacaoRepository;

    @Inject
    IgrejaRepository igrejaRepository;

    @Inject
    EventoRepository eventoRepository;

    @Override
    public List<TransacaoResponseDTO> listarTransacoes(Long igrejaId) {

        return transacaoRepository.findByIgrejaId(igrejaId).stream()
                .map(TransacaoResponseDTO::new)
                .toList();
    }

    @Override
    public List<TransacaoResponseDTO> listarTransacoesEvento(Long eventoId) {
        return transacaoRepository.findByEventoId(eventoId).stream()
                .map(TransacaoResponseDTO::new)
                .toList();
    }

    @Override
    @Transactional
    public TransacaoResponseDTO realizarTransacao(TransacaoRequestDTO dto) {
        Igreja igreja = igrejaRepository.findByIdOptional(dto.igrejaId())
                .orElseThrow(() -> new WebApplicationException("Igreja não encontrada com o ID: " + dto.igrejaId(), Response.Status.NOT_FOUND));

        Evento evento = new Evento();
        if(dto.eventoId() != null){
             evento = eventoRepository.findByIdOptional(dto.eventoId())
                    .orElseThrow(() -> new WebApplicationException("Evento não encontrado com o ID: " + dto.eventoId(), Response.Status.NOT_FOUND));
        }

        // 2. Cria a nova entidade Transacao a partir do DTO
        Transacao novaTransacao = new Transacao(dto);

        // 3. Associa a transação à igreja correta
        novaTransacao.setIgreja(igreja);
        if(dto.eventoId() != null){
            novaTransacao.setEvento(evento);
        }

        // 4. Persiste a transação no banco de dados
        transacaoRepository.persist(novaTransacao);

        // 5. Retorna um DTO de resposta com os dados da transação criada
        return new TransacaoResponseDTO(novaTransacao);
    }

}
