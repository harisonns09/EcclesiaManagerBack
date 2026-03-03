package org.ecclesiaManager.model.dto;

import org.ecclesiaManager.model.Evento;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public record InscricaoRequestDTO(

        String nome,
        String email,
        String telefone,
        String cpf

) {
}

