package org.ecclesiaManager.model.dto.dashboard;

import org.ecclesiaManager.model.dto.EventoResponseDTO;
import java.util.List;

public record DashboardResponseDTO(
        DashboardResumoDTO resumo,
        List<AniversarianteDTO> aniversariantes,
        List<EventoResponseDTO> proximosEventos
) {}