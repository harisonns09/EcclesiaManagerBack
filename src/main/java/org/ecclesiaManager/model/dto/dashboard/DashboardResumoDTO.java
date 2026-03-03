package org.ecclesiaManager.model.dto.dashboard;

public record DashboardResumoDTO(
        long totalMembros,
        long totalVisitantes,
        long totalMinisterios,
        long totalEventosFuturos
) {}