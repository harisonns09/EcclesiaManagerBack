package org.ecclesiaManager.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.ecclesiaManager.enums.StatusCheckIn;
import org.ecclesiaManager.model.CheckInKids;
import org.ecclesiaManager.model.dto.CheckInKidsRequestDTO;
import org.ecclesiaManager.model.dto.CheckInKidsResponseDTO;
import org.ecclesiaManager.repository.CheckInKidsRepository;
import org.ecclesiaManager.repository.PessoaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class CheckInKidsServiceImpl implements ICheckInKidsService {

    @Inject
    CheckInKidsRepository checkInKidsRepository;

    @Inject
    PessoaRepository pessoaRepository;

    @Override
    @Transactional
    public CheckInKidsResponseDTO realizarCheckIn(Long igrejaId, CheckInKidsRequestDTO dto) {
        String codigo = gerarCodigoSeguranca();
        CheckInKids checkIn = new CheckInKids(dto, codigo);
        checkInKidsRepository.persist(checkIn);
        return new CheckInKidsResponseDTO(checkIn);
    }

    @Override
    @Transactional
    public void realizarCheckOut(Long checkInKidsId) {
        CheckInKids checkIn = checkInKidsRepository.findByIdOptional(checkInKidsId)
                .orElseThrow(() -> new IllegalArgumentException("Check-in não encontrado"));

        checkIn.setStatus(StatusCheckIn.FINALIZADO);
        checkIn.setDataSaida(LocalDateTime.now());
    }

    @Override
    public List<CheckInKidsResponseDTO> listarAtivos(Long igrejaId) {
        return checkInKidsRepository.findByIgrejaIdAndStatusOrderByDataEntradaDesc(igrejaId, StatusCheckIn.ATIVO)
                .stream()
                .map(CheckInKidsResponseDTO::new)
                .toList();
    }

    private String gerarCodigoSeguranca() {
        Random random = new Random();
        int numero = 1000 + random.nextInt(9000);
        return "K-" + numero;
    }
}