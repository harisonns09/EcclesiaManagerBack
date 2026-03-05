package org.ecclesiaManager.controller;

import org.ecclesiaManager.infra.audit.Loggable;
import org.ecclesiaManager.model.Ministerio;
import org.ecclesiaManager.model.dto.MinisterioRequestDTO;
import org.ecclesiaManager.model.dto.MinisterioResponseDTO;
import org.ecclesiaManager.service.IMinisterioService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/ministerios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MinisterioController {

    @Inject
    IMinisterioService ministerioService;

    @GET
    @Path("/igrejas/{igrejaId}")
    public List<MinisterioResponseDTO> getMinisterios(@PathParam("igrejaId") Long igrejaId) {
        return ministerioService.findAllByIgreja(igrejaId);
    }

    @GET
    @Path("/igrejas/{igrejaId}/{id}")
    public Response carregarMinisterio(@PathParam("id") Long id) {
        return ministerioService.findById(id)
                .map(registro -> Response.ok(registro).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/igrejas/{igrejaId}")
    @Loggable(action = "Criou", entity = "Ministerio")

    public Response addMinisterio(@PathParam("igrejaId") Long igrejaId, @Valid MinisterioRequestDTO ministerio) {
        try {
            return Response.ok(ministerioService.addMinisterio(igrejaId, ministerio)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/igrejas/{igrejaId}/{id}")
    @Loggable(action = "Deletou", entity = "Ministerio")

    public Response deleteMinisterio(@PathParam("igrejaId") Long igrejaId, @PathParam("id") Long id) {
        ministerioService.deleteById(igrejaId, id);
        return Response.ok().build();
    }

    @PUT
    @Path("/igrejas/{igrejaId}/{id}")
    @Loggable(action = "Alterou", entity = "Ministerio")

    public Response updateMinisterio(@PathParam("igrejaId") Long igrejaId, @PathParam("id") Long id, MinisterioRequestDTO dto) {
        return Response.ok(ministerioService.updateMinisterio(igrejaId, id, dto)).build();
    }
}