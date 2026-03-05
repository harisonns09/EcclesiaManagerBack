package org.ecclesiaManager.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ecclesiaManager.model.dto.TransacaoRequestDTO;
import org.ecclesiaManager.model.dto.TransacaoResponseDTO;
import org.ecclesiaManager.service.ITransacaoService;

import java.util.List;

@Path("/api/transacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransacaoController {

    @Inject
    ITransacaoService transacaoService;

    @GET
    @Path("/igrejas/{igrejaId}")
    public Response listarTransacoes(@PathParam("igrejaId") Long igrejaId) {
        List<TransacaoResponseDTO> response = transacaoService.listarTransacoes(igrejaId);
        return Response.ok(response).build();
    }

    @GET
    @Path("/eventos/{eventoId}")
    public Response listarTransacoesEvento(@PathParam("eventoId") Long eventoId) {
        List<TransacaoResponseDTO> response = transacaoService.listarTransacoesEvento(eventoId);
        return Response.ok(response).build();
    }

    @POST
    public Response realizarTransacao(@Valid TransacaoRequestDTO dto) {
        TransacaoResponseDTO response = transacaoService.realizarTransacao(dto);
        return Response.ok(response).build();
    }
}
