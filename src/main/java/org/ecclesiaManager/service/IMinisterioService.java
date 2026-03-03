package org.ecclesiaManager.service;

import org.ecclesiaManager.model.Ministerio;
import org.ecclesiaManager.model.dto.MinisterioRequestDTO;
import org.ecclesiaManager.model.dto.MinisterioResponseDTO;

import java.util.List;
import java.util.Optional;

public interface IMinisterioService {

    public List<MinisterioResponseDTO> findAllByIgreja(Long igrejaId);
    public Optional<Ministerio> findById(Long id);
    public MinisterioResponseDTO addMinisterio(Long igrejaId, MinisterioRequestDTO ministerio);
    public void deleteById(Long idIgreja, Long idMinisterio);
    public MinisterioResponseDTO updateMinisterio(Long idIgreja, Long idMinisterio, MinisterioRequestDTO dto);
}
