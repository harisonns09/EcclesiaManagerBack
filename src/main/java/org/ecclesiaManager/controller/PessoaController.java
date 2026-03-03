package org.ecclesiaManager.controller;

import org.ecclesiaManager.model.dto.PageResponseDTO;
import org.ecclesiaManager.model.dto.PessoaRequestDTO;
import org.ecclesiaManager.model.dto.PessoaResponseDTO;
import org.ecclesiaManager.model.dto.TrilhaRequestDTO;
import org.ecclesiaManager.service.IPessoaService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PessoaController {

    @Inject
    IPessoaService pessoaService;

    @GET
    @Path("/igrejas/{igrejaId}/membros")
    public List<PessoaResponseDTO> getMembros(@PathParam("igrejaId") Long igrejaId) {
        return pessoaService.findAllByIgrejaId(igrejaId);
    }

    @GET
    @Path("/igrejas/{igrejaId}/membros/{id}")
    public Response carregarPessoa(@PathParam("igrejaId") Long igrejaId, @PathParam("id") Long id) {
        return pessoaService.findById(igrejaId, id)
                .map(registro -> Response.ok(registro).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/igrejas/{igrejaId}/membros")
    public Response addPessoa(@PathParam("igrejaId") Long igrejaId, @Valid PessoaRequestDTO pessoa) {
        return Response.ok(pessoaService.addPessoa(igrejaId, pessoa)).build();
    }

    @DELETE
    @Path("/igrejas/{igrejaId}/membros/{id}")
    public Response deletePessoa(@PathParam("igrejaId") Long igrejaId, @PathParam("id") Long id) {
        pessoaService.deleteById(igrejaId, id);
        return Response.ok().build();
    }

    @PUT
    @Path("/igrejas/{igrejaId}/membros/{id}")
    public Response updatePessoa(@PathParam("igrejaId") Long igrejaId, @PathParam("id") Long id, @Valid PessoaRequestDTO dto) {
        return Response.ok(pessoaService.update(igrejaId, id, dto)).build();
    }

    @POST
    @Path("/public/{igrejaId}/visitantes")
    public Response cadastrarVisitante(@PathParam("igrejaId") Long igrejaId, @Valid PessoaRequestDTO dto) {
        return Response.ok(pessoaService.addPessoa(igrejaId, dto)).build();
    }

    @GET
    @Path("/igrejas/{igrejaId}/visitantes")
    public List<PessoaResponseDTO> getVisitantes(@PathParam("igrejaId") Long igrejaId) {
        return pessoaService.findAllVisitorsByIgrejaId(igrejaId);
    }

    @POST
    @Path("/public/{igrejaId}/membros")
    public Response addPessoaPublic(@PathParam("igrejaId") Long igrejaId, @Valid PessoaRequestDTO pessoa) {
        return Response.ok(pessoaService.addPessoa(igrejaId, pessoa)).build();
    }

    @GET
    @Path("/igrejas/{igrejaId}/membros/paginado")
    public Response getMembersPaged(
            @PathParam("igrejaId") Long igrejaId,
            @QueryParam("nome") String nome,
            @QueryParam("genero") String genero,
            @QueryParam("mesAniversario") Integer mesAniversario,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {

        PageResponseDTO<PessoaResponseDTO> pageData = pessoaService.findPaged(
                igrejaId, nome, genero, mesAniversario, page, size
        );

        return Response.ok(pageData).build();
    }

    @PUT
    @Path("/igrejas/{igrejaId}/membros/{id}/trilha")
    public Response atualizarTrilha(@PathParam("igrejaId") Long igrejaId, @PathParam("id") Long id, @Valid TrilhaRequestDTO dto) {
        return Response.ok(pessoaService.atualizarTrilha(igrejaId, id, dto)).build();
    }
}