package com.alex.helpdesk.service;

import com.alex.helpdesk.dto.UsuarioRequestDTO;
import com.alex.helpdesk.dto.UsuarioResponseDTO;
import com.alex.helpdesk.exception.UsuarioNaoEncontradoException;
import com.alex.helpdesk.model.Usuario;
import com.alex.helpdesk.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponseDTO cadastrarUsuario(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario(dto);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return converterParaDTO(usuarioSalvo);
    }

    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        return converterParaDTO(usuario);
    }

    private UsuarioResponseDTO converterParaDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }
}