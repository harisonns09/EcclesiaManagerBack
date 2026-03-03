package org.ecclesiaManager.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.ecclesiaManager.infra.audit.Loggable;
import org.ecclesiaManager.model.Igreja;
import org.ecclesiaManager.model.Ministerio;
import org.ecclesiaManager.model.dto.MinisterioRequestDTO;
import org.ecclesiaManager.model.dto.MinisterioResponseDTO;
import org.ecclesiaManager.repository.IgrejaRepository;
import org.ecclesiaManager.repository.MinisterioRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MinisterioServiceImpl implements IMinisterioService {

    @Inject
    MinisterioRepository ministerioRepository;

    @Inject
    IgrejaRepository igrejaRepository;

    @Override
    public List<MinisterioResponseDTO> findAllByIgreja(Long igrejaId) {
        return ministerioRepository.findByIgrejaId(igrejaId).stream()
                .map(MinisterioResponseDTO::new)
                .toList();
    }

    @Override
    public Optional<Ministerio> findById(Long id) {
        return ministerioRepository.findByIdOptional(id);
    }

    @Override
    @Transactional
    @Loggable(action = "CRIAR", entity = "MINISTERIO")
    public MinisterioResponseDTO addMinisterio(Long igrejaId, MinisterioRequestDTO dto) {
        Igreja igreja = igrejaRepository.findByIdOptional(igrejaId)
                .orElseThrow(() -> new RuntimeException("Igreja não encontrada com ID: " + igrejaId));

        Ministerio ministerio = new Ministerio(dto, igreja);
        ministerioRepository.persist(ministerio);

        return new MinisterioResponseDTO(ministerio);
    }

    @Override
    @Transactional
    @Loggable(action = "DELETAR", entity = "MINISTERIO")
    public void deleteById(Long idIgreja, Long idMinisterio) {
        Ministerio ministerio = ministerioRepository.findByIdOptional(idMinisterio)
                .orElseThrow(() -> new RuntimeException("Ministério não encontrado com ID: " + idMinisterio));

        if (!ministerio.getIgreja().getId().equals(idIgreja)) {
            throw new RuntimeException("Operação não permitida. O ministério não pertence a esta igreja.");
        }
        ministerioRepository.delete(ministerio);
    }

    @Override
    @Transactional
    @Loggable(action = "ALTERAR", entity = "MINISTERIO")
    public MinisterioResponseDTO updateMinisterio(Long idIgreja, Long idMinisterio, MinisterioRequestDTO dto) {
        Ministerio ministerio = ministerioRepository.findByIdOptional(idMinisterio)
                .orElseThrow(() -> new RuntimeException("Ministerio não encontrado com ID: " + idMinisterio));

        if(!ministerio.getIgreja().getId().equals(idIgreja)) {
            throw new RuntimeException("Ministério não pertence a esta Igreja");
        }

        ministerio.setNome(dto.nome());
        ministerio.setLiderResponsavel(dto.liderResponsavel());

        return new MinisterioResponseDTO(ministerio);
    }
}