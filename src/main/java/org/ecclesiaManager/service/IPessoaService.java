package org.ecclesiaManager.service;

import org.ecclesiaManager.model.dto.PageResponseDTO;
import org.ecclesiaManager.model.dto.PessoaRequestDTO;
import org.ecclesiaManager.model.dto.PessoaResponseDTO;
import org.ecclesiaManager.model.dto.TrilhaRequestDTO;

import java.util.List;
import java.util.Optional;

public interface IPessoaService {

    List<PessoaResponseDTO> findAllByIgrejaId(Long igrejaId);

    List<PessoaResponseDTO> findByNome(String nome);

    Optional<PessoaResponseDTO> findById(Long igrejaId, Long pessoaId);

    PessoaResponseDTO addPessoa(Long igrejaId, PessoaRequestDTO dto);

    void deleteById(Long igrejaId, Long pessoaId);

    PessoaResponseDTO update(Long igrejaId, Long pessoaId, PessoaRequestDTO dto);

    List<PessoaResponseDTO> findAllVisitorsByIgrejaId(Long igrejaId);

    PageResponseDTO<PessoaResponseDTO> findPaged(Long igrejaId, String nome, String genero, Integer mesAniversario, int pageIndex, int pageSize);

    PessoaResponseDTO atualizarTrilha(Long igrejaId, Long pessoaId, TrilhaRequestDTO dto);
}