package org.ecclesiaManager.controller;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Parameters;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ecclesiaManager.model.AuditLog;
import org.ecclesiaManager.model.dto.PageResponseDTO;
import org.ecclesiaManager.repository.AuditLogRepository;

import java.util.List;

@Path("/api/audit")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuditController {

    @Inject
    AuditLogRepository auditLogRepository;

    @GET
    @Path("/{churchId}")
    public Response getLogs(
            @PathParam("churchId") Long churchId,
            @QueryParam("status") String status,       // Filtro: SUCCESS ou FAILED
            @QueryParam("username") String username,   // Filtro: Por operador
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {

        // 1. Construção da Query Dinâmica
        StringBuilder query = new StringBuilder("churchId = :churchId");
        Parameters params = Parameters.with("churchId", churchId);

        if (status != null && !status.isEmpty()) {
            query.append(" and status = :status");
            params.and("status", status);
        }

        if (username != null && !username.isEmpty()) {
            query.append(" and username = :username");
            params.and("username", username);
        }

        // 2. Executa a busca paginada e ordenada por data (mais recente primeiro)
        var panacheQuery = auditLogRepository.find(query.toString(),
                        Sort.by("timestamp").descending(),
                        params)
                .page(Page.of(page, size));

        // 3. Monta o seu PageResponseDTO com os dados reais do banco
        PageResponseDTO<AuditLog> response = new PageResponseDTO<>(
                panacheQuery.list(),           // A lista de logs da página atual
                panacheQuery.pageCount(),      // Total de páginas disponíveis
                panacheQuery.count()           // Total de registros no banco
        );

        return Response.ok(response).build();
    }
}