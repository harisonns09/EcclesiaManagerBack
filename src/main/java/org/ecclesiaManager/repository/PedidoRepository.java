package org.ecclesiaManager.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.ecclesiaManager.model.Pedido;

import java.util.List;

@ApplicationScoped
public class PedidoRepository implements PanacheRepositoryBase<Pedido, Long> {

    public List<Pedido> findAllByIgrejaId(Long igrejaId) {
        return list("igreja.id", igrejaId);
    }

    public List<Pedido> findAllByCompradorId(Long compradorId) {
        // Assume que a propriedade na sua entidade Pedido se chama "comprador"
        return list("comprador.id", compradorId);
    }
}