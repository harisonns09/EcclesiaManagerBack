package org.ecclesiaManager.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.ecclesiaManager.model.Evento;

import java.util.List;

@ApplicationScoped
public class EventoRepository implements PanacheRepositoryBase<Evento, Long> {

    public List<Evento> findByIgrejaId(Long idIgreja) {
        return list("igreja.id", idIgreja);
    }

    public long countEventosFuturosByIgreja(Long igrejaId, java.time.LocalDate dataAtual) {
        return count("igreja.id = ?1 and dataEvento >= ?2", igrejaId, dataAtual);
    }
    public List<Evento> findProximosEventos(Long igrejaId, java.time.LocalDate dataAtual) {
        // Pega apenas os 5 próximos eventos
        return find("igreja.id = ?1 and dataEvento >= ?2 order by dataEvento asc", igrejaId, dataAtual)
                .page(io.quarkus.panache.common.Page.ofSize(5))
                .list();
    }
}