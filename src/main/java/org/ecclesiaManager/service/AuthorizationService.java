package org.ecclesiaManager.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.ecclesiaManager.model.Usuario;
import org.ecclesiaManager.repository.UsuarioRepository;

@ApplicationScoped
public class AuthorizationService {

    @Inject
    UsuarioRepository usuarioRepository;

    public Usuario loadUserByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }
}