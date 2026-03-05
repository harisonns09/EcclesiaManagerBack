package org.ecclesiaManager.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.ecclesiaManager.model.Perfil;

@ApplicationScoped
public class PerfilRepository implements PanacheRepository<Perfil> {
    public Perfil findByNome(String nome) {
        return find("nome", nome).firstResult();
    }
}
