package org.ecclesiaManager.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.ecclesiaManager.model.Transacao;

import java.util.List;

@ApplicationScoped
public class TransacaoRepository implements PanacheRepository<Transacao> {

    public List<Transacao> findByEventoId(Long eventoId) {
        return list("evento.id", eventoId);
    }

    public List<Transacao> findByIgrejaId(Long igrejaId) {
        return list("igreja.id", igrejaId);
    }

}
