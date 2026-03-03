package org.ecclesiaManager.controller;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ecclesiaManager.infra.security.TokenService;
import org.ecclesiaManager.model.Usuario;
import org.ecclesiaManager.model.dto.AuthenticationDTO;
import org.ecclesiaManager.model.dto.LoginResponseDTO;
import org.ecclesiaManager.repository.UsuarioRepository;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenticationController {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    TokenService tokenService;

    @POST
    @Path("/login")
    @PermitAll
    public Response login(@Valid AuthenticationDTO data) {
        Usuario usuario = usuarioRepository.findByUsername(data.login());

        if (usuario == null || !BcryptUtil.matches(data.password(), usuario.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Usuário ou senha inválidos")
                    .build();
        }

        String token = tokenService.generateToken(usuario);

        return Response.ok(new LoginResponseDTO(token)).build();
    }
}