package org.ecclesiaManager.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.ecclesiaManager.model.Pessoa;

import java.util.List;

@ApplicationScoped
public class PessoaRepository implements PanacheRepositoryBase<Pessoa, Long> {

    public List<Pessoa> findByNome(String nome) {
        return list("nome", nome);
    }

    public List<Pessoa> findAllByIgrejaId(Long igrejaId) {
        return list("igreja.id = ?1 and status = 'Ativo' order by nome asc", igrejaId);
    }

    public List<Pessoa> findAllVisitantesByIgreja(Long igrejaId) {
        return list("igreja.id = ?1 and status = 'Visitante'", Sort.by("id"), igrejaId);
    }

    public long countMembrosByIgreja(Long igrejaId) {
        return count("igreja.id = ?1 and status = 'Ativo'", igrejaId);
    }
    public long countVisitantesByIgreja(Long igrejaId) {
        return count("igreja.id = ?1 and status = 'Visitante'", igrejaId);
    }
    public List<Pessoa> findAniversariantesDoMes(Long igrejaId, int mes) {
        return list("igreja.id = ?1 and month(dataNascimento) = ?2 order by day(dataNascimento)", igrejaId, mes);
    }
}