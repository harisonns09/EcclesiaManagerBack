package org.ecclesiaManager.model.dto;

import java.util.List;

public record CompradorDTO(
        String nome,
        String email,
        String cpf,
        String telefone
) {}