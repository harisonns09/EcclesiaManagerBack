package org.ecclesiaManager.controller;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ecclesiaManager.enums.UserRole;
import org.ecclesiaManager.model.dto.PerfilResponseDTO;
import org.ecclesiaManager.model.dto.UsuarioRequestDTO;
import org.ecclesiaManager.model.dto.UsuarioResponseDTO;
import org.ecclesiaManager.service.IUsuarioService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class UsuarioController {

    @Inject
    IUsuarioService usuarioService;

    @GET
    @Path("/roles")
    public Response getRoles() {

        List<String> nomesPerfis = usuarioService.carregarPerfis().stream()
                .map(perfil -> perfil.nome()) // Extrai o nome do record
                .collect(Collectors.toList());

        // 3. Devolve a lista de Strings para o React
        return Response.ok(nomesPerfis).build();
    }

    @GET
    @Path("/permissoes")
    public Response getPermissoes() {
        // Retorna direto a List<String> com as permissões cadastradas
        return Response.ok(usuarioService.carregarPermissoes()).build();
    }

    @GET
    @Path("/{igrejaId}")
    public List<UsuarioResponseDTO> listarUsuarios(@PathParam("igrejaId") Long igrejaId) {
        return usuarioService.listarUsuarios(igrejaId);
    }

    @POST
    @Path("/{igrejaId}")
    public Response register(@PathParam("igrejaId") Long igrejaId, @Valid UsuarioRequestDTO data) {
        // Criando um novo DTO para garantir que o igrejaId do path seja usado
        UsuarioRequestDTO request = new UsuarioRequestDTO(data.id(), data.user(), data.password(), data.perfil(), data.permissions(), igrejaId);
        return usuarioService.register(request);
    }

    @PUT
    @Path("/{igrejaId}/{userId}")
    public Response alterarUsuario(@PathParam("igrejaId") Long igrejaId, @PathParam("userId") Long userId, @Valid UsuarioRequestDTO data) {
        UsuarioResponseDTO response = usuarioService.alterarUsuario(igrejaId, userId, data);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{igrejaId}/{userId}")
    public Response deleteUsuario(@PathParam("igrejaId") Long igrejaId, @PathParam("userId") Long userId) {
        usuarioService.deleteUsuario(igrejaId, userId);
        return Response.noContent().build();
    }
}