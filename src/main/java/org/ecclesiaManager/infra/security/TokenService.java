package org.ecclesiaManager.infra.security;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.ecclesiaManager.model.Permissao;
import org.ecclesiaManager.model.Usuario;

import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class TokenService {

    public String generateToken(Usuario usuario) {
        try {
            Set<String> permissoes = new HashSet<>();

            // 1. Adiciona o nome do Perfil Base (ex: "ADMIN", "TESOUREIRO")
            if (usuario.getPerfil() != null) {
                permissoes.add(usuario.getPerfil().getNome());

                // 2. Adiciona as permissões embutidas no Perfil Base
                if (usuario.getPerfil().getPermissoes() != null) {
                    for (Permissao p : usuario.getPerfil().getPermissoes()) {
                        permissoes.add(p.getNome());
                    }
                }
            }

            // 3. 🔥 NOVO: Adiciona as permissões extras individuais do usuário (Checkboxes do Front)
            if (usuario.getPermissoes() != null) {
                for (Permissao p : usuario.getPermissoes()) {
                    permissoes.add(p.getNome());
                }
            }

            Long churchId = usuario.getIgreja() != null ? usuario.getIgreja().getId() : 0L;

            return Jwt.issuer("auth-api")
                    .subject(usuario.getUsername())
                    .upn(usuario.getUsername())
                    .claim("id", usuario.getId())
                    .claim("nome", usuario.getUsername())
                    .claim("churchId", churchId)
                    // Passa a lista FINAL (Perfil + Permissoes do Perfil + Permissoes Extras)
                    .groups(permissoes)
                    .expiresAt(System.currentTimeMillis() / 1000 + 3600) // 1 hora
                    .sign();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar token", e);
        }
    }
}