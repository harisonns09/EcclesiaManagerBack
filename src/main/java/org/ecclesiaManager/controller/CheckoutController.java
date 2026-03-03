package org.ecclesiaManager.controller;

import org.ecclesiaManager.model.dto.CheckoutRequestDTO;
import org.ecclesiaManager.model.dto.CheckoutResponseDTO;
import org.ecclesiaManager.service.IEventoService;
import org.ecclesiaManager.service.InfinitePayService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CheckoutController {

    @Inject
    InfinitePayService infinitePayService;

    @Inject
    IEventoService eventoService;

    @POST
    @Path("/eventos/{eventId}/checkout")
    public Response createCheckout(
            @PathParam("eventId") String eventId,
            CheckoutRequestDTO data
    ) {
        CheckoutResponseDTO response = infinitePayService.createCheckoutLink(eventId, data);
        return Response.ok(response).build();
    }
}