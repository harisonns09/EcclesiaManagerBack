package org.ecclesiaManager.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ecclesiaManager.model.Pedido;
import org.ecclesiaManager.model.dto.PedidoRequestDTO;
import org.ecclesiaManager.service.IPedidoService;

import java.util.List;

@Path("/api/v1/igrejas/{igrejaId}/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoController {

    @Inject
    IPedidoService pedidoService;

    @POST
    public Response realizarPedido(@PathParam("igrejaId") Long igrejaId, PedidoRequestDTO dto) {
        Pedido pedido = pedidoService.processarVenda(igrejaId, dto);
        return Response.status(Response.Status.CREATED).entity(pedido).build();
    }

    @GET
    public Response listar(@PathParam("igrejaId") Long igrejaId) {
        List<Pedido> pedidos = pedidoService.listar(igrejaId);
        return Response.ok(pedidos).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("igrejaId") Long igrejaId, @PathParam("id") Long id) {
        Pedido pedido = pedidoService.buscarPorId(igrejaId, id);
        return Response.ok(pedido).build();
    }
}