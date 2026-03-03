package org.ecclesiaManager.model.dto;

import org.ecclesiaManager.model.CheckInKids;

import java.time.LocalDateTime;

public record CheckInKidsResponseDTO(
        Long id,
        String nomeCrianca,
        String nomeResponsavel,
        String telefoneResponsavel,
        String codigoSeguranca,
        LocalDateTime dataEntrada,
        String observacoes,
        String status
) {
    public CheckInKidsResponseDTO(CheckInKids checkInKids){
        this(
                checkInKids.getId(),
                checkInKids.getCrianca(),
                checkInKids.getResponsavel(),
                checkInKids.getTelefoneResponsavel(),
                checkInKids.getCodigoSeguranca(),
                checkInKids.getDataEntrada(),
                checkInKids.getObservacoes(),
                checkInKids.getStatus().toString()
        );
    }
}
