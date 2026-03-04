package org.ecclesiaManager.controller;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ecclesiaManager.enums.UserRole;
import org.ecclesiaManager.model.dto.UsuarioRequestDTO;
import org.ecclesiaManager.model.dto.UsuarioResponseDTO;
import org.ecclesiaManager.service.IUsuarioService;

import java.util.Arrays;
import java.util.List;

@Path("/api/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class UsuarioController {

    @Inject
    IUsuarioService usuarioService;

    @GET
    @Path("/roles")
    @Produces(MediaType.APPLICATION_JSON) // Adicionado para consistência
    public List<String> listarRoles() {
        return Arrays.stream(UserRole.values())
                .map(Enum::name)
                .toList();
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
        UsuarioRequestDTO request = new UsuarioRequestDTO(data.id(), data.user(), data.password(), data.role(), igrejaId);
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