package org.ecclesiaManager.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import org.ecclesiaManager.model.AuditLog;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AuditLogRepository implements PanacheRepositoryBase<AuditLog, Long> {

    public PanacheQuery<AuditLog> findByChurchId(Long churchId, Page page) {
        return find("churchId", Sort.by("timestamp").descending(), churchId)
                .page(page);
    }

    public PanacheQuery<AuditLog> findByChurchIdAndUsername(Long churchId, String username, Page page) {
        return find("churchId = ?1 and username = ?2", Sort.by("timestamp").descending(), churchId, username)
                .page(page);
    }

}