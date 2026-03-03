package org.ecclesiaManager.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ecclesiaManager.model.dto.InscricaoResponseDTO;
import org.ecclesiaManager.service.IInscricaoService;

import java.util.List;
import java.util.Map;

@Path("/api/inscricoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InscricaoController {

    @Inject
    IInscricaoService inscricaoService;

    @GET
    @Path("/{cpf}")
    public Response buscarInscricoes(@PathParam("cpf") String cpf) {
        List<InscricaoResponseDTO> inscricoes = inscricaoService.buscarInscricoesPorCpf(cpf);

        if (inscricoes.isEmpty()) {
            return Response.noContent().build();
        }

        return Response.ok(inscricoes).build();
    }

    @PUT
    @Path("/confirmarPagamento/{idEvento}/{idInscricao}")
    public Response confirmarPagamento(
            @PathParam("idEvento") Long idEvento,
            @PathParam("idInscricao") String idInscricao,
            Map<String, String> payload) {

        String tipoValor = payload.get("tipoValor");

        inscricaoService.confirmarPagamento(idInscricao, tipoValor);

        return Response.ok().build();
    }
}