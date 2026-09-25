package org.ecclesiaManager.controller;

import jakarta.batch.operations.JobOperator;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.util.Properties;

@Path("/api/batch")
public class BatchController {

    @Inject
    JobOperator jobOperator;

    @POST
    @Path("/testar-notificacoes")
    public Response testarNotificacoes() {
        // Dispara o job manualmente
        long executionId = jobOperator.start("notificacoes-job", new Properties());

        return Response.ok("Job de notificações iniciado com sucesso! Execution ID: " + executionId).build();
    }
}