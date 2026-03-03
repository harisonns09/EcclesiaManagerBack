package org.ecclesiaManager.controller;

import jakarta.annotation.security.RolesAllowed;
import org.ecclesiaManager.model.dto.EventoRequestDTO;
import org.ecclesiaManager.model.dto.EventoResponseDTO;
import org.ecclesiaManager.model.dto.InscricaoRequestDTO;
import org.ecclesiaManager.service.IEventoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventoController {

    @Inject
    IEventoService eventoService;

    @GET
    @Path("/igrejas/{idIgreja}/eventos")
    public List<EventoResponseDTO> getEventos(@PathParam("idIgreja") Long idIgreja) {
        return eventoService.findByIgrejaId(idIgreja);
    }

    @GET
    @Path("/igrejas/{idIgreja}/eventos/{id}")
    public Response carregarEvento(@PathParam("idIgreja") Long idIgreja, @PathParam("id") Long id) {
        return eventoService.findById(idIgreja, id)
                .map(registro -> Response.ok(registro).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/igrejas/{idIgreja}/eventos")
    @RolesAllowed({"ADMIN", "LIDER"})
    public Response addEvento(@PathParam("idIgreja") Long idIgreja, @Valid EventoRequestDTO evento) {
        return Response.ok(eventoService.addEvento(idIgreja, evento)).build();
    }

    @DELETE
    @Path("/igrejas/{idIgreja}/eventos/{id}")
    @RolesAllowed({"ADMIN", "LIDER"})
    public Response deleteEvento(@PathParam("idIgreja") Long idIgreja, @PathParam("id") Long id) {
        eventoService.deleteById(idIgreja, id);
        return Response.ok().build();
    }

    @PUT
    @Path("/igrejas/{idIgreja}/eventos/{id}")
    @RolesAllowed({"ADMIN", "LIDER"})
    public Response updateEvento(@PathParam("idIgreja") Long idIgreja, @PathParam("id") Long id, @Valid EventoRequestDTO dto) {
        return Response.ok(eventoService.update(idIgreja, id, dto)).build();
    }

    @POST
    @Path("/eventos/{id}/inscricao")
    public Response realizarInscricao(@PathParam("id") Long id, @Valid InscricaoRequestDTO dto) {
        try {
            return Response.ok(eventoService.realizarInscricao(id, dto)).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("message", e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/igrejas/{idIgreja}/eventos/{idEvento}/inscricoes/{idInscricao}/pagamento")
    @RolesAllowed({"ADMIN", "TESOUREIRO"})
    public Response atualizarFormaPagamento(
            @PathParam("idIgreja") String idIgreja,
            @PathParam("idEvento") Long idEvento,
            @PathParam("idInscricao") String idInscricao,
            Map<String, String> payload
    ) {
        String formaPagamento = payload.get("formaPagamento");
        eventoService.atualizarMetodoPagamento(idInscricao, formaPagamento);
        return Response.ok().build();
    }
}