package org.ecclesiaManager.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.ecclesiaManager.model.Inscricao;

import java.util.List;

@ApplicationScoped
public class InscricaoRepository implements PanacheRepositoryBase<Inscricao, Long> {

    public List<Inscricao> findByEventoId(Long eventoId) {
        return list("evento.id", eventoId);
    }

    public Inscricao findByNumero_Inscricao(String numero_inscricao) {
        return find("numeroInscricao", numero_inscricao).firstResult();
    }

    public List<Inscricao> findAllByCpf(String cpf) {
        return list("cpf", cpf);
    }

    public boolean existsByCpfAndEventoId(String cpf, Long eventoId) {
        long count = count("cpf = ?1 and evento.id = ?2", cpf, eventoId);
        return count > 0;
    }

    public void deleteAllByEventoId(Long eventoId) {
        delete("evento.id", eventoId);
    }
}