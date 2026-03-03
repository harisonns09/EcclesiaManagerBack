package org.ecclesiaManager.service;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.ecclesiaManager.infra.audit.Loggable;
import org.ecclesiaManager.model.Igreja;
import org.ecclesiaManager.model.Usuario;
import org.ecclesiaManager.model.dto.UsuarioRequestDTO;
import org.ecclesiaManager.model.dto.UsuarioResponseDTO;
import org.ecclesiaManager.repository.IgrejaRepository;
import org.ecclesiaManager.repository.UsuarioRepository;

import java.util.List;

@ApplicationScoped
public class UsuarioServiceImpl implements IUsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    IgrejaRepository igrejaRepository;

    @Override
    @Transactional
    @Loggable(action = "CRIAR", entity = "USUARIO")
    public Response register(UsuarioRequestDTO data) {
        if (this.usuarioRepository.findByUsername(data.user()) != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Usuário já existe").build();
        }

        Igreja igreja = igrejaRepository.findByIdOptional(data.igrejaId())
                .orElseThrow(() -> new WebApplicationException("Igreja não encontrada", Response.Status.NOT_FOUND));

        String encryptedPassword = BcryptUtil.bcryptHash(data.password());
        Usuario newUser = new Usuario(data.user(), encryptedPassword, data.role(), igreja);
        this.usuarioRepository.persist(newUser);

        return Response.ok().build();
    }

    @Override
    public List<UsuarioResponseDTO> listarUsuarios(Long igrejaId) {
        return usuarioRepository.findByIgrejaId(igrejaId).stream()
                .map(UsuarioResponseDTO::new)
                .toList();
    }

    @Override
    @Transactional
    @Loggable(action = "ALTERAR", entity = "USUARIO")
    public UsuarioResponseDTO alterarUsuario(Long igrejaId, Long userId, UsuarioRequestDTO data) {
        Usuario usuario = usuarioRepository.findByIdOptional(userId)
                .filter(u -> u.getIgreja().getId().equals(igrejaId))
                .orElseThrow(() -> new WebApplicationException("Usuário não encontrado ou não pertence a esta igreja", Response.Status.NOT_FOUND));

        if (data.role() != null) {
            usuario.setRole(data.role());
        }
        if (data.password() != null && !data.password().isBlank()) {
            String encryptedPassword = BcryptUtil.bcryptHash(data.password());
            usuario.setPassword(encryptedPassword);
        }

        return new UsuarioResponseDTO(usuario);
    }

    @Override
    @Transactional
    @Loggable(action = "DELETAR", entity = "USUARIO")
    public void deleteUsuario(Long igrejaId, Long userId) {
        Usuario usuario = usuarioRepository.findByIdOptional(userId)
                .filter(u -> u.getIgreja().getId().equals(igrejaId))
                .orElseThrow(() -> new WebApplicationException("Usuário não encontrado ou não pertence a esta igreja", Response.Status.NOT_FOUND));

        usuarioRepository.delete(usuario);
    }
}