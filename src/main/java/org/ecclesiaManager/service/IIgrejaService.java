package org.ecclesiaManager.service;

import org.ecclesiaManager.model.dto.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface IIgrejaService {

    List<IgrejaResponseDTO> findAll();
    Optional<IgrejaResponseDTO> findById(Long id);
    IgrejaResponseDTO save(IgrejaRequestDTO igreja);
    IgrejaResponseDTO update(Long id, IgrejaRequestDTO igreja);
    void delete(Long id);
}
