package org.ecclesiaManager.controller;

import io.quarkus.panache.common.Page;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ecclesiaManager.model.AuditLog;
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
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {

        List<AuditLog> logs = auditLogRepository.findByChurchId(churchId, Page.of(page, size))
                .list();

        return Response.ok(logs).build();
    }
}