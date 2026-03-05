package org.ecclesiaManager.service;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.ecclesiaManager.infra.audit.Loggable;
import org.ecclesiaManager.model.Igreja;
import org.ecclesiaManager.model.Perfil;
import org.ecclesiaManager.model.Permissao;
import org.ecclesiaManager.model.Usuario;
import org.ecclesiaManager.model.dto.PerfilResponseDTO;
import org.ecclesiaManager.model.dto.UsuarioRequestDTO;
import org.ecclesiaManager.model.dto.UsuarioResponseDTO;
import org.ecclesiaManager.repository.IgrejaRepository;
import org.ecclesiaManager.repository.PerfilRepository;
import org.ecclesiaManager.repository.PermissaoRepository;
import org.ecclesiaManager.repository.UsuarioRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class UsuarioServiceImpl implements IUsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    IgrejaRepository igrejaRepository;

    @Inject
    PerfilRepository perfilRepository;

    @Inject
    PermissaoRepository permissaoRepository;

    @Override
    @Transactional
    public Response register(UsuarioRequestDTO data) {
        // Verifica se o usuário já existe
        if (this.usuarioRepository.findByUsername(data.user()) != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Usuário já existe").build();
        }

        // Busca a Igreja
        Igreja igreja = igrejaRepository.findByIdOptional(data.igrejaId())
                .orElseThrow(() -> new WebApplicationException("Igreja não encontrada", Response.Status.NOT_FOUND));

        // 1. Busca o Perfil base no banco de dados
        Perfil perfil = perfilRepository.findByNome(data.perfil());
        if (perfil == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Perfil inválido ou não encontrado").build();
        }

        // 2. Cria o usuário com a senha criptografada e o novo Perfil
        String encryptedPassword = BcryptUtil.bcryptHash(data.password());
        Usuario newUser = new Usuario(data.user(), encryptedPassword, perfil, igreja);

        // 3. Processa as permissões extras (Checkboxes do Frontend)
        Set<Permissao> permissoesExtras = new HashSet<>();
        if (data.permissions() != null && !data.permissions().isEmpty()) {
            for (String nomePermissao : data.permissions()) {
                Permissao p = permissaoRepository.findByNome(nomePermissao);
                if (p != null) {
                    permissoesExtras.add(p);
                }
            }
        }
        newUser.setPermissoes(permissoesExtras);

        // 4. Salva no banco de dados
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
    public UsuarioResponseDTO alterarUsuario(Long igrejaId, Long userId, UsuarioRequestDTO data) {
        Usuario usuario = usuarioRepository.findByIdOptional(userId)
                .filter(u -> u.getIgreja().getId().equals(igrejaId))
                .orElseThrow(() -> new WebApplicationException("Usuário não encontrado ou não pertence a esta igreja", Response.Status.NOT_FOUND));

        if (data.perfil() != null && !data.perfil().isBlank()) {
            Perfil perfil = perfilRepository.findByNome(data.perfil());
            if (perfil == null) {
                throw new WebApplicationException("Perfil inválido ou não encontrado", Response.Status.BAD_REQUEST);
            }
            usuario.setPerfil(perfil);
        }

        if (data.permissions() != null) {
            Set<Permissao> permissoesExtras = new HashSet<>();
            for (String nomePermissao : data.permissions()) {
                Permissao p = permissaoRepository.findByNome(nomePermissao);
                if (p != null) {
                    permissoesExtras.add(p);
                }
            }
            usuario.setPermissoes(permissoesExtras);
        }

        if (data.password() != null && !data.password().isBlank()) {
            String encryptedPassword = BcryptUtil.bcryptHash(data.password());
            usuario.setPassword(encryptedPassword);
        }

        return new UsuarioResponseDTO(usuario);
    }

    @Override
    @Transactional
    public void deleteUsuario(Long igrejaId, Long userId) {
        Usuario usuario = usuarioRepository.findByIdOptional(userId)
                .filter(u -> u.getIgreja().getId().equals(igrejaId))
                .orElseThrow(() -> new WebApplicationException("Usuário não encontrado ou não pertence a esta igreja", Response.Status.NOT_FOUND));

        usuarioRepository.delete(usuario);
    }

    public List<PerfilResponseDTO> carregarPerfis() {
        return perfilRepository.listAll().stream()
                .map(PerfilResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> carregarPermissoes() {
        return permissaoRepository.listAll().stream()
                .map(Permissao::getNome)
                .collect(Collectors.toList()); // Retorna apenas os nomes (ex: "VER_FINANCEIRO")
    }
}