package org.ecclesiaManager.repository;

import org.ecclesiaManager.model.Igreja;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class IgrejaRepository implements PanacheRepositoryBase<Igreja, Long> {
}
