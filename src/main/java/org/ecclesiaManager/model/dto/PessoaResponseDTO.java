package org.ecclesiaManager.model.dto;

import org.ecclesiaManager.model.Pessoa;
import java.time.LocalDate;

public record PessoaResponseDTO(
        Long id,
        Long churchId,
        String nome,
        String email,
        String telefone,
        LocalDate dataNascimento,
        String ministerio,
        String status,

        String genero,
        String estadoCivil,
        String cep,
        String endereco,
        String numero,
        String bairro,
        String cidade,
        String estado,
        String complemento,
        LocalDate dataBatismo,
        String observacao,
        int progressoTrilha,
        Boolean trilhaCafeConcluido,
        Boolean trilhaCelulaConcluida,
        Boolean trilhaClasseConcluida
) {
    public PessoaResponseDTO(Pessoa pessoa) {
        this(
                pessoa.getId(),
                pessoa.getIgreja().getId(),
                pessoa.getNome(),
                pessoa.getEmail(),
                pessoa.getTelefone(),
                pessoa.getDataNascimento(),
                pessoa.getMinisterio(),
                pessoa.getStatus(),
                pessoa.getGenero(),
                pessoa.getEstadoCivil(),
                pessoa.getCep(),
                pessoa.getEndereco(),
                pessoa.getNumero(),
                pessoa.getBairro(),
                pessoa.getCidade(),
                pessoa.getEstado(),
                pessoa.getComplemento(),
                pessoa.getDataBatismo(),
                pessoa.getObservacao(),
                pessoa.getTrilha() != null ? pessoa.getTrilha().calcularProgresso() : 0,
                pessoa.getTrilha() != null ? pessoa.getTrilha().getCafePastorConcluido() : false,
                pessoa.getTrilha() != null ? pessoa.getTrilha().getVisitaCelulaConcluida() : false,
                pessoa.getTrilha() != null ? pessoa.getTrilha().getClasseIntegracaoConcluida() : false
        );
    }
}