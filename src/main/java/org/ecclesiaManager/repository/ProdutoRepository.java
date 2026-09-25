package org.ecclesiaManager.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.ecclesiaManager.model.Produto;

import java.util.List;

@ApplicationScoped
public class ProdutoRepository implements PanacheRepositoryBase<Produto, Long> {

    public List<Produto> findAllByIgrejaId(Long igrejaId) {
        // "igreja.id" tem de corresponder exatamente ao nome da variável
        // e da relação definida na sua entidade Produto
        return list("igreja.id", igrejaId);
    }

    public List<Produto> findAllByIgrejaIdAndAtivoTrue(Long igrejaId) {
        // O Panache substitui o ?1 pelo primeiro parâmetro passado (igrejaId)
        return list("igreja.id = ?1 and ativo = true", igrejaId);
    }
}