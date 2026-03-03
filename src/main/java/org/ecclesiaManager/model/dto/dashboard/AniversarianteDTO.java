package org.ecclesiaManager.model.dto.dashboard;

import org.ecclesiaManager.model.Pessoa;
import java.time.LocalDate;

public record AniversarianteDTO(
        Long id, String nome, LocalDate dataNascimento, String telefone, String ministerio
) {
    public AniversarianteDTO(Pessoa pessoa) {
        this(pessoa.getId(), pessoa.getNome(), pessoa.getDataNascimento(), pessoa.getTelefone(), pessoa.getMinisterio());
    }
}