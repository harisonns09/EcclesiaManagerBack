package org.ecclesiaManager.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.ecclesiaManager.model.Evento;
import org.ecclesiaManager.model.Igreja;
import org.ecclesiaManager.model.dto.EventoResponseDTO;
import org.ecclesiaManager.model.dto.IgrejaRequestDTO;
import org.ecclesiaManager.model.dto.IgrejaResponseDTO;
import org.ecclesiaManager.repository.IgrejaRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class IgrejaServiceImpl implements IIgrejaService{

    @Inject
    private IgrejaRepository igrejaRepository;

    @Override
    public List<IgrejaResponseDTO> findAll() {
        return igrejaRepository.findAll().stream().map(
                igreja -> new IgrejaResponseDTO(igreja)).toList(
        );
    }

    @Override
    public Optional<IgrejaResponseDTO> findById(Long id) {
        return igrejaRepository.findByIdOptional(id)
                .map(igreja -> new IgrejaResponseDTO(igreja));
    }

    @Override
    @Transactional
    public IgrejaResponseDTO save(IgrejaRequestDTO dto) {
        Igreja newIgreja = new Igreja(dto);

        igrejaRepository.persist(newIgreja);
        return new IgrejaResponseDTO(newIgreja);

    }

    @Override
    public IgrejaResponseDTO update(Long id, IgrejaRequestDTO dto) {

        Igreja igreja = igrejaRepository.findByIdOptional(id)
                .orElseThrow(() -> new IllegalArgumentException("Igreja não encontrada"));


        igreja.setName(dto.name());
        igreja.setAddress(dto.address());
        igreja.setCity(dto.city());
        igreja.setState(dto.state());
        igreja.setInstagram(dto.instagram());

        return new IgrejaResponseDTO(igreja);

    }

    @Override
    @Transactional
    public void delete(Long id) {

        igrejaRepository.deleteById(id);
    }
}
