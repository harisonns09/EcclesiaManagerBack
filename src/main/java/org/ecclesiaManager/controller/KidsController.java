package org.ecclesiaManager.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ecclesiaManager.model.dto.CheckInKidsRequestDTO;
import org.ecclesiaManager.model.dto.CheckInKidsResponseDTO;
import org.ecclesiaManager.service.ICheckInKidsService;

import java.util.List;

@Path("/api/kids")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN", "KIDS"})
public class KidsController {

    @Inject
    ICheckInKidsService checkInKidsService;

    @POST
    @Path("/{igrejaId}/checkin")
    public Response checkin(@PathParam("igrejaId") Long igrejaId, CheckInKidsRequestDTO dto) {
        return Response.ok(checkInKidsService.realizarCheckIn(igrejaId, dto)).build();
    }

    @GET
    @Path("/{igrejaId}/ativos")
    public Response listarAtivos(@PathParam("igrejaId") Long igrejaId) {
        return Response.ok(checkInKidsService.listarAtivos(igrejaId)).build();
    }

    @POST
    @Path("/{igrejaId}/checkout/{checkInId}")
    public Response checkout(@PathParam("checkInId") Long checkInId) {
        checkInKidsService.realizarCheckOut(checkInId);
        return Response.ok().build();
    }
}