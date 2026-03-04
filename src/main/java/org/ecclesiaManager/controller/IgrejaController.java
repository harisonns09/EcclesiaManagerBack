package org.ecclesiaManager.controller;

import jakarta.annotation.security.PermitAll;
import org.ecclesiaManager.model.dto.IgrejaRequestDTO;
import org.ecclesiaManager.model.dto.IgrejaResponseDTO;
import org.ecclesiaManager.service.IIgrejaService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/igrejas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IgrejaController {

    @Inject
    IIgrejaService igrejaService;

    @GET

    @PermitAll
    public List<IgrejaResponseDTO> findAll() {
        return igrejaService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return igrejaService.findById(id)
                .map(igreja -> Response.ok(igreja).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/igreja")
    public Response addIgreja(@Valid IgrejaRequestDTO igreja) {
        return Response.ok(igrejaService.save(igreja)).build();
    }

    @PUT
    @Path("/igreja/{id}")
    public Response update(@PathParam("id") Long id, @Valid IgrejaRequestDTO dto) {
        return Response.ok(igrejaService.update(id, dto)).build();
    }

    @DELETE
    @Path("/igreja/{id}")
    public Response delete(@PathParam("id") Long id) {
        igrejaService.delete(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/publico/{id}")
    public Response findPublicById(@PathParam("id") Long id) {
        return igrejaService.findById(id)
                .map(igreja -> Response.ok(igreja).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}