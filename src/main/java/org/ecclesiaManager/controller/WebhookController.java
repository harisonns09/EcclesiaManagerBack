package org.ecclesiaManager.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ecclesiaManager.model.dto.infinitepay.InfinitePayWebhookDTO;
import org.ecclesiaManager.service.IEventoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/api/webhooks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @Inject
    IEventoService eventoService;

    @POST
    @Path("/infinitepay")
    public Response handleInfinitePayWebhook(InfinitePayWebhookDTO payload) {

        logger.info("Webhook InfinitePay recebido. Order NSU: {}", payload.orderNsu());

        try {
            eventoService.processarPagamentoWebhook(payload);

            return Response.ok().build();
        } catch (Exception e) {
            logger.error("Erro ao processar webhook", e);

            return Response.serverError().build();
        }
    }
}