package org.ecclesiaManager.service;

import org.ecclesiaManager.model.dto.PerfilResponseDTO;
import org.ecclesiaManager.model.dto.UsuarioRequestDTO;
import org.ecclesiaManager.model.dto.UsuarioResponseDTO;
import jakarta.ws.rs.core.Response;

import java.util.List;

public interface IUsuarioService {

    Response register(UsuarioRequestDTO data);

    List<UsuarioResponseDTO> listarUsuarios(Long igrejaId);

    UsuarioResponseDTO alterarUsuario(Long igrejaId, Long userId, UsuarioRequestDTO data);

    void deleteUsuario(Long igrejaId, Long userId);

    List<PerfilResponseDTO> carregarPerfis();

    Object carregarPermissoes();
}