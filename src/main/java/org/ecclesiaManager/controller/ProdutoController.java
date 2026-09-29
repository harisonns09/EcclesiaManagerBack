package org.ecclesiaManager.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ecclesiaManager.model.Produto;
import org.ecclesiaManager.model.dto.ProdutoRequestDTO;
import org.ecclesiaManager.service.IProdutoService;

import java.util.List;

@Path("/api/v1/igrejas/{igrejaId}/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProdutoController {

    @Inject
    IProdutoService produtoService;

    @POST
    public Response criar(@PathParam("igrejaId") Long igrejaId, ProdutoRequestDTO dto) {
        Produto produto = produtoService.criar(igrejaId, dto);
        return Response.status(Response.Status.CREATED).entity(produto).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("igrejaId") Long igrejaId, @PathParam("id") Long id, ProdutoRequestDTO dto) {
        Produto produto = produtoService.atualizar(igrejaId, id, dto);
        return Response.ok(produto).build();
    }

    @GET
    public Response listar(@PathParam("igrejaId") Long igrejaId) {
        List<Produto> produtos = produtoService.listar(igrejaId);
        return Response.ok(produtos).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("igrejaId") Long igrejaId, @PathParam("id") Long id) {
        Produto produto = produtoService.buscarPorId(igrejaId, id);
        return Response.ok(produto).build();
    }
}