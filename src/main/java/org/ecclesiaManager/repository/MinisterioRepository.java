package org.ecclesiaManager.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.ecclesiaManager.model.Ministerio;
import java.util.List;

@ApplicationScoped
public class MinisterioRepository implements PanacheRepositoryBase<Ministerio, Long> {

    public List<Ministerio> findByIgrejaId(Long igrejaId) {
        return list("igreja.id", igrejaId);
    }

    public List<Ministerio> findByNome(String nome) {

        return list("nome", nome);
    }

    public long countByIgreja(Long igrejaId) {
        return count("igreja.id = ?1", igrejaId);
    }
}