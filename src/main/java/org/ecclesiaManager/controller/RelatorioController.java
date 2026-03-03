package org.ecclesiaManager.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ecclesiaManager.service.IRelatorioService;

@Path("/api/relatorios")
public class RelatorioController {

    @Inject
    IRelatorioService relatorioService;

    @GET
    @Path("/eventos/{eventoId}/excel")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response baixarListaInscritos(@PathParam("eventoId") Long eventoId) {
        byte[] arquivo = relatorioService.gerarPlanilhaInscritos(eventoId);

        String nomeArquivo = "inscritos_evento_" + eventoId + ".xlsx";

        return Response.ok(arquivo)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nomeArquivo)
                .build();
    }
}