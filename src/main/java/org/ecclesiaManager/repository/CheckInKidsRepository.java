package org.ecclesiaManager.repository;

import org.ecclesiaManager.enums.StatusCheckIn;
import org.ecclesiaManager.model.CheckInKids;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CheckInKidsRepository implements PanacheRepositoryBase<CheckInKids, Long> {

    public List<CheckInKids> findByIgrejaIdAndStatusOrderByDataEntradaDesc(Long igrejaId, StatusCheckIn status) {

        return list("igrejaId = ?1 and status = ?2", Sort.by("dataEntrada").descending(), igrejaId, status);
    }
}