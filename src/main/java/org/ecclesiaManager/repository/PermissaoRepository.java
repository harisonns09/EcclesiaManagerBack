package org.ecclesiaManager.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.ecclesiaManager.model.Perfil;
import org.ecclesiaManager.model.Permissao;

@ApplicationScoped
public class PermissaoRepository implements PanacheRepository<Permissao> {
    public Permissao findByNome(String nome) {
        return find("nome", nome).firstResult();
    }
}
