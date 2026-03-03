package org.ecclesiaManager.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.ecclesiaManager.infra.audit.Loggable;
import org.ecclesiaManager.model.Igreja;
import org.ecclesiaManager.model.Pessoa;
import org.ecclesiaManager.model.TrilhaIntegracao;
import org.ecclesiaManager.model.dto.PageResponseDTO;
import org.ecclesiaManager.model.dto.PessoaRequestDTO;
import org.ecclesiaManager.model.dto.PessoaResponseDTO;
import org.ecclesiaManager.model.dto.TrilhaRequestDTO;
import org.ecclesiaManager.repository.IgrejaRepository;
import org.ecclesiaManager.repository.PessoaRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PessoaServiceImpl implements IPessoaService {

    @Inject
    PessoaRepository pessoaRepository;

    @Inject
    IgrejaRepository igrejaRepository;

    @Override
    public List<PessoaResponseDTO> findAllByIgrejaId(Long igrejaId) {
        return pessoaRepository.findAllByIgrejaId(igrejaId).stream()
                .map(PessoaResponseDTO::new)
                .toList();
    }

    @Override
    public List<PessoaResponseDTO> findByNome(String nome) {
        return pessoaRepository.findByNome(nome).stream()
                .map(PessoaResponseDTO::new)
                .toList();
    }

    @Override
    public Optional<PessoaResponseDTO> findById(Long igrejaId, Long pessoaId) {
        return pessoaRepository.findByIdOptional(pessoaId)
                .filter(pessoa -> pessoa.getIgreja().getId().equals(igrejaId))
                .map(PessoaResponseDTO::new);
    }

    @Override
    @Transactional
    @Loggable(action = "CRIAR", entity = "MEMBRO")
    public PessoaResponseDTO addPessoa(Long igrejaId, PessoaRequestDTO dto) {
        Igreja igreja = igrejaRepository.findByIdOptional(igrejaId)
                .orElseThrow(() -> new WebApplicationException("Igreja não encontrada", Response.Status.NOT_FOUND));

        Pessoa newPessoa = new Pessoa(dto);
        newPessoa.setIgreja(igreja);
        pessoaRepository.persist(newPessoa);
        return new PessoaResponseDTO(newPessoa);
    }

    @Override
    @Transactional
    public void deleteById(Long igrejaId, Long pessoaId) {
        Pessoa pessoa = pessoaRepository.findByIdOptional(pessoaId)
                .filter(p -> p.getIgreja().getId().equals(igrejaId))
                .orElseThrow(() -> new WebApplicationException("Pessoa não encontrada ou não pertence a esta igreja", Response.Status.NOT_FOUND));
        pessoaRepository.delete(pessoa);
    }

    @Override
    @Transactional
    @Loggable(action = "ALTERAR", entity = "MEMBRO")
    public PessoaResponseDTO update(Long igrejaId, Long pessoaId, PessoaRequestDTO dto) {
        Pessoa pessoa = pessoaRepository.findByIdOptional(pessoaId)
                .filter(p -> p.getIgreja().getId().equals(igrejaId))
                .orElseThrow(() -> new WebApplicationException("Pessoa não encontrada ou não pertence a esta igreja", Response.Status.NOT_FOUND));

        pessoa.setNome(dto.nome());
        pessoa.setDataNascimento(dto.dataNascimento());
        pessoa.setTelefone(dto.telefone());
        pessoa.setEmail(dto.email());
        pessoa.setMinisterio(dto.ministerio());
        pessoa.setStatus(dto.status());
        pessoa.setGenero(dto.genero());
        pessoa.setEstadoCivil(dto.estadoCivil());
        pessoa.setCep(dto.cep());
        pessoa.setEndereco(dto.endereco());
        pessoa.setNumero(dto.numero());
        pessoa.setBairro(dto.bairro());
        pessoa.setCidade(dto.cidade());
        pessoa.setEstado(dto.estado());
        pessoa.setComplemento(dto.complemento());
        pessoa.setDataBatismo(dto.dataBatismo());

        return new PessoaResponseDTO(pessoa);
    }

    @Override
    public List<PessoaResponseDTO> findAllVisitorsByIgrejaId(Long igrejaId) {
        return pessoaRepository.findAllVisitantesByIgreja(igrejaId)
                .stream()
                .map(PessoaResponseDTO::new)
                .toList();
    }

    @Override
    public PageResponseDTO<PessoaResponseDTO> findPaged(Long igrejaId, String nome, String genero, Integer mes, int pageIndex, int pageSize) {
        StringBuilder query = new StringBuilder("igreja.id = :igrejaId");
        Parameters params = Parameters.with("igrejaId", igrejaId);

        if (nome != null && !nome.isBlank()) {
            query.append(" and lower(nome) like :nome");
            params.and("nome", "%" + nome.toLowerCase() + "%");
        }

        if (genero != null && !genero.isBlank()) {
            query.append(" and genero = :genero");
            params.and("genero", genero);
        }

        if (mes != null && mes > 0) {
            query.append(" and month(dataNascimento) = :mes");
            params.and("mes", mes);
        }

        query.append(" order by lower(nome) asc");

        PanacheQuery<Pessoa> pagedQuery = pessoaRepository.find(query.toString(), params)
                .page(io.quarkus.panache.common.Page.of(pageIndex, pageSize));

        List<PessoaResponseDTO> content = pagedQuery.stream()
                .map(PessoaResponseDTO::new)
                .toList();

        return new PageResponseDTO<>(
                content,
                pagedQuery.pageCount(),
                pagedQuery.count()
        );
    }

    @Override
    @Transactional
    @Loggable(action = "ATUALIZAR_TRILHA", entity = "MEMBRO")
    public PessoaResponseDTO atualizarTrilha(Long igrejaId, Long pessoaId, TrilhaRequestDTO dto) {
        Pessoa pessoa = pessoaRepository.findByIdOptional(pessoaId)
                .filter(p -> p.getIgreja().getId().equals(igrejaId))
                .orElseThrow(() -> new WebApplicationException("Pessoa não encontrada ou não pertence a esta igreja", Response.Status.NOT_FOUND));

        if (pessoa.getTrilha() == null) {
            pessoa.setTrilha(new TrilhaIntegracao());
        }

        TrilhaIntegracao trilha = pessoa.getTrilha();
        trilha.setDecidiuSerMembro(dto.decidiuSerMembro());
        trilha.setDataDecisao(dto.dataDecisao());
        trilha.setCafePastorConcluido(dto.cafePastorConcluido());
        trilha.setVisitaCelulaConcluida(dto.visitaCelulaConcluida());
        trilha.setClasseIntegracaoConcluida(dto.classeIntegracaoConcluida());
        trilha.setDataApresentacao(dto.dataApresentacao());

        if (dto.dataApresentacao() != null) {
            pessoa.setStatus("Ativo");
        }

        return new PessoaResponseDTO(pessoa);
    }
}