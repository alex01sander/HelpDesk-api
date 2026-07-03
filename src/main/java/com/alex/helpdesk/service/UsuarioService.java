package com.alex.helpdesk.service;

import com.alex.helpdesk.dto.UsuarioRequestDTO;
import com.alex.helpdesk.dto.UsuarioResponseDTO;
import com.alex.helpdesk.model.Usuario;
import com.alex.helpdesk.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponseDTO cadastrarUsuario(UsuarioRequestDTO dto) {

        Usuario usuario = new Usuario(dto);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(
                usuarioSalvo.getId(),
                usuarioSalvo.getNome(),
                usuarioSalvo.getEmail()
        );
    }
}