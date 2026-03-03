package org.ecclesiaManager.service;

import org.ecclesiaManager.model.dto.CheckInKidsRequestDTO;
import org.ecclesiaManager.model.dto.CheckInKidsResponseDTO;

import java.util.List;

public interface ICheckInKidsService {

    public CheckInKidsResponseDTO realizarCheckIn(Long igrejaId, CheckInKidsRequestDTO dto);
    public void realizarCheckOut(Long checkInKidsId);
    public List<CheckInKidsResponseDTO> listarAtivos(Long igrejaId);


}
