package org.ecclesiaManager.infra.security;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.ecclesiaManager.model.Usuario;

import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class TokenService {




    public String generateToken(Usuario usuario) {
        try {
            Long churchId = usuario.getIgreja() != null ? usuario.getIgreja().getId() : 0L;

            return Jwt.issuer("auth-api")
                    .subject(usuario.getUsername())
                    .upn(usuario.getUsername())
                    .claim("id", usuario.getId())
                    .groups(Set.of(usuario.getRole().name()))
                    .claim("role", usuario.getRole().name())
                    .claim("churchId", churchId)
                    .expiresIn(Duration.ofHours(1))
                    .sign();
//                    .signWithSecret(secret); // Agora assina com o texto puro corretamente

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar token", e);
        }
    }
}