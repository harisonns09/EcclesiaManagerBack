package org.ecclesiaManager.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.ecclesiaManager.enums.StatusPagamento;
import org.ecclesiaManager.model.Evento;
import org.ecclesiaManager.model.Igreja;
import org.ecclesiaManager.model.Inscricao;
import org.ecclesiaManager.model.dto.EventoRequestDTO;
import org.ecclesiaManager.model.dto.EventoResponseDTO;
import org.ecclesiaManager.model.dto.InscricaoRequestDTO;
import org.ecclesiaManager.model.dto.InscricaoResponseDTO;
import org.ecclesiaManager.model.dto.infinitepay.InfinitePayWebhookDTO;
import org.ecclesiaManager.repository.EventoRepository;
import org.ecclesiaManager.repository.IgrejaRepository;
import org.ecclesiaManager.repository.InscricaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EventoServiceImpl implements IEventoService {

    private static final Logger log = LoggerFactory.getLogger(EventoServiceImpl.class);

    @Inject
    EventoRepository eventoRepository;

    @Inject
    InscricaoRepository inscricaoRepository;

    @Inject
    IgrejaRepository igrejaRepository;

    private String cleanDigits(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.replaceAll("[^0-9]", "");
    }

    @Override
    @Transactional
    public List<EventoResponseDTO> findByIgrejaId(Long idIgreja) {
        return eventoRepository.findByIgrejaId(idIgreja).stream()
                .map(EventoResponseDTO::new)
                .toList();
    }

    @Override
    @Transactional
    public Optional<EventoResponseDTO> findById(Long eventoId) {
        return eventoRepository.findByIdOptional(eventoId)
                .map(EventoResponseDTO::new);
    }

    @Override
    @Transactional
    public EventoResponseDTO addEvento(Long idIgreja, EventoRequestDTO dto) {
        Igreja igreja = igrejaRepository.findByIdOptional(idIgreja)
                .orElseThrow(() -> new WebApplicationException("Igreja não encontrada", Response.Status.NOT_FOUND));

        Evento newEvento = new Evento(dto);
        newEvento.setIgreja(igreja);

        eventoRepository.persist(newEvento);
        log.info("Novo evento criado: ID={} | Nome='{}' | IgrejaID={}", newEvento.getId(), newEvento.getNomeEvento(), igreja.getId());

        return new EventoResponseDTO(newEvento);
    }

    @Override
    @Transactional
    public void deleteById(Long idIgreja, Long eventoId) {
        Evento evento = eventoRepository.findByIdOptional(eventoId)
                .filter(e -> e.getIgreja().getId().equals(idIgreja))
                .orElseThrow(() -> new WebApplicationException("Evento não encontrado ou não pertence a esta igreja", Response.Status.NOT_FOUND));

        log.info("Solicitada exclusão do evento ID: {}", eventoId);
        inscricaoRepository.deleteAllByEventoId(eventoId);
        eventoRepository.delete(evento);
        log.info("Evento ID: {} excluído com sucesso.", eventoId);
    }

    @Override
    @Transactional
    public EventoResponseDTO update(Long idIgreja, Long eventoId, EventoRequestDTO dto) {
        Evento evento = eventoRepository.findByIdOptional(eventoId)
                .filter(e -> e.getIgreja().getId().equals(idIgreja))
                .orElseThrow(() -> new WebApplicationException("Evento não encontrado ou não pertence a esta igreja", Response.Status.NOT_FOUND));

        evento.setNomeEvento(dto.nomeEvento());
        evento.setDataEvento(dto.dataEvento());
        evento.setDescricao(dto.descricao());
        evento.setHorario(dto.horario());
        evento.setLocal(dto.local());
        evento.setPreco(dto.preco());
        evento.setMinisterioResponsavel(dto.ministerioResponsavel());

        log.info("Evento ID: {} atualizado com sucesso.", eventoId);
        return new EventoResponseDTO(evento);
    }

    @Override
    @Transactional
    public InscricaoResponseDTO realizarInscricao(Long eventoId, InscricaoRequestDTO dto) {
        Evento evento = eventoRepository.findByIdOptional(eventoId)
                .orElseThrow(()-> new WebApplicationException("Evento não encontrado", Response.Status.NOT_FOUND));

        String cpfLimpo = cleanDigits(dto.cpf());
        String telefoneLimpo = cleanDigits(dto.telefone());

        if (inscricaoRepository.existsByCpfAndEventoId(cpfLimpo, eventoId)) {
            log.warn("Tentativa de inscrição duplicada bloqueada. CPF: {} | Evento: {}", cpfLimpo, eventoId);
            throw new WebApplicationException("Este CPF já possui uma inscrição confirmada ou pendente para este evento.", Response.Status.CONFLICT);
        }

        Inscricao newInscricao = new Inscricao(dto, evento);
        newInscricao.setCpf(cpfLimpo);
        newInscricao.setTelefone(telefoneLimpo);
        inscricaoRepository.persist(newInscricao);

        String dataHoje = newInscricao.getDataInscricao().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String numeroGerado = dataHoje + eventoId + newInscricao.getId();
        newInscricao.setNumeroInscricao(numeroGerado);

        log.info("Inscrição realizada: #{} | Evento: {} | Nome: {}", numeroGerado, evento.getNomeEvento(), newInscricao.getNome());

        return new InscricaoResponseDTO(newInscricao);
    }

    @Override
    @Transactional
    public void processarPagamentoWebhook(InfinitePayWebhookDTO payload) {
        String numeroInscricao = payload.orderNsu();
        log.info("Webhook InfinitePay recebido. NSU/Inscrição: {}", numeroInscricao);

        if (numeroInscricao == null) {
            log.error("Webhook recebido sem OrderNSU (ID Inscrição). Payload: {}", payload);
            return;
        }

        Inscricao inscricao = inscricaoRepository.findByNumero_Inscricao(numeroInscricao);
        if (inscricao == null) {
            log.error("Inscrição não encontrada para o número: {}", numeroInscricao);
            return;
        }

        if (!"PAGO".equals(inscricao.getStatus())) {
            inscricao.setStatus(StatusPagamento.PAGO.getStatusPagamento());
            inscricao.setDataPagamento(java.time.LocalDateTime.now());
            inscricao.setComprovante(payload.receiptUrl());
            inscricao.setValorPago(inscricao.getEvento().getPreco());
            log.info("Pagamento confirmado com sucesso via Webhook para inscrição: #{}", numeroInscricao);
        } else {
            log.warn("Webhook ignorado: Inscrição #{} já consta como PAGO.", numeroInscricao);
        }
    }

    @Override
    @Transactional
    public void atualizarMetodoPagamento(String idInscricao, String tipoPagamento) {
        Inscricao inscricao = inscricaoRepository.findByNumero_Inscricao(idInscricao);
        if (inscricao == null) {
            log.error("Inscrição não encontrada ao tentar atualizar método de pagamento: {}", idInscricao);
            return;
        }

        if (!tipoPagamento.equals(inscricao.getTipoPagamento())) {
            String tipoAntigo = inscricao.getTipoPagamento();
            inscricao.setTipoPagamento(tipoPagamento);
            log.info("Método de pagamento alterado na inscrição #{}. De '{}' para '{}'", idInscricao, tipoAntigo, tipoPagamento);
        }
    }
}