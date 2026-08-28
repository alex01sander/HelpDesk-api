package com.alex.helpdesk.service;

import com.alex.helpdesk.dto.ComentarioRequestDTO;
import com.alex.helpdesk.dto.ComentarioResponseDTO;
import com.alex.helpdesk.exception.ChamadoNaoEncontradoException;
import com.alex.helpdesk.exception.TecnicoNaoEncontradoException;
import com.alex.helpdesk.exception.UsuarioNaoEncontradoException;
import com.alex.helpdesk.model.Chamado;
import com.alex.helpdesk.model.Comentario;
import com.alex.helpdesk.model.Role;
import com.alex.helpdesk.model.Tecnico;
import com.alex.helpdesk.model.Usuario;
import com.alex.helpdesk.repository.ChamadoRepository;
import com.alex.helpdesk.repository.ComentarioRepository;
import com.alex.helpdesk.repository.TecnicoRepository;
import com.alex.helpdesk.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TecnicoRepository tecnicoRepository;

    public ComentarioService(ComentarioRepository comentarioRepository,
                             ChamadoRepository chamadoRepository,
                             UsuarioRepository usuarioRepository,
                             TecnicoRepository tecnicoRepository) {
        this.comentarioRepository = comentarioRepository;
        this.chamadoRepository = chamadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    @Transactional
    public ComentarioResponseDTO adicionarComentario(Long chamadoId, ComentarioRequestDTO dto) {
        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new ChamadoNaoEncontradoException(chamadoId));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .findFirst()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .orElse("");

        Usuario autorUsuario = null;
        Tecnico autorTecnico = null;

        if (role.equals(Role.USUARIO.name())) {
            autorUsuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new UsuarioNaoEncontradoException(email));
        } else if (role.equals(Role.TECNICO.name())) {
            autorTecnico = tecnicoRepository.findByEmail(email)
                    .orElseThrow(() -> new TecnicoNaoEncontradoException(email));
        }

        Comentario comentario = new Comentario(dto.texto(), chamado, autorUsuario, autorTecnico);
        Comentario comentarioSalvo = comentarioRepository.save(comentario);

        return converterParaDTO(comentarioSalvo);
    }

    public List<ComentarioResponseDTO> listarComentarios(Long chamadoId) {
        chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new ChamadoNaoEncontradoException(chamadoId));

        return comentarioRepository.findByChamadoId(chamadoId)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    private ComentarioResponseDTO converterParaDTO(Comentario comentario) {
        String autorNome;
        String tipoAutor;

        if (comentario.getAutorUsuario() != null) {
            autorNome = comentario.getAutorUsuario().getNome();
            tipoAutor = "USUARIO";
        } else {
            autorNome = comentario.getAutorTecnico().getNome();
            tipoAutor = "TECNICO";
        }

        return new ComentarioResponseDTO(
                comentario.getId(),
                comentario.getTexto(),
                comentario.getCreatedAt(),
                autorNome,
                tipoAutor
        );
    }
}