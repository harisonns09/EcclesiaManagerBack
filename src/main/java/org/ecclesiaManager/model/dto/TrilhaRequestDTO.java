package org.ecclesiaManager.model.dto;
import java.time.LocalDate;

public record TrilhaRequestDTO(
        Boolean decidiuSerMembro,
        LocalDate dataDecisao,
        Boolean cafePastorConcluido,
        Boolean visitaCelulaConcluida,
        Boolean classeIntegracaoConcluida,
        LocalDate dataApresentacao
) {}