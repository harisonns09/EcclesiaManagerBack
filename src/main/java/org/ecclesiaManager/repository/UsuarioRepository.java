package org.ecclesiaManager.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.ecclesiaManager.model.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import java.util.List;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepositoryBase<Usuario, Long> {

    public Usuario findByUsername(String username) {
        return find("username", username).firstResult();
    }

    public List<Usuario> findByIgrejaId(Long igrejaId) {
        return list("igreja.id", igrejaId);
    }
}