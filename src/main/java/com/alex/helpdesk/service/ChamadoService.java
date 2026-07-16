package com.alex.helpdesk.service;

import com.alex.helpdesk.dto.ChamadoRequestDTO;
import com.alex.helpdesk.dto.ChamadoResponseDTO;
import com.alex.helpdesk.dto.TecnicoResponseDTO;
import com.alex.helpdesk.dto.UsuarioResponseDTO;
import com.alex.helpdesk.exception.ChamadoNaoEncontradoException;
import com.alex.helpdesk.exception.UsuarioNaoEncontradoException;
import com.alex.helpdesk.model.Chamado;
import com.alex.helpdesk.model.Tecnico;
import com.alex.helpdesk.model.Usuario;
import com.alex.helpdesk.repository.ChamadoRepository;
import com.alex.helpdesk.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;

    public ChamadoService(ChamadoRepository chamadoRepository, UsuarioRepository usuarioRepository) {
        this.chamadoRepository = chamadoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ChamadoResponseDTO abrirChamado(ChamadoRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException(dto.usuarioId()));

        Chamado chamado = new Chamado(
                dto.titulo(),
                dto.descricao(),
                dto.prioridade(),
                usuario
        );

        Chamado chamadoSalvo = chamadoRepository.save(chamado);

        return converterParaDTO(chamadoSalvo);
    }

    public List<ChamadoResponseDTO> listarChamados() {
        return chamadoRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    private ChamadoResponseDTO converterParaDTO(Chamado chamado) {
        UsuarioResponseDTO usuarioDTO = new UsuarioResponseDTO(
                chamado.getUsuario().getId(),
                chamado.getUsuario().getNome(),
                chamado.getUsuario().getEmail()
        );

        TecnicoResponseDTO tecnicoDTO = null;
        if (chamado.getTecnico() != null) {
            Tecnico tecnico = chamado.getTecnico();
            tecnicoDTO = new TecnicoResponseDTO(
                    tecnico.getId(),
                    tecnico.getNome(),
                    tecnico.getEmail(),
                    tecnico.getEspecialidade()
            );
        }

        return new ChamadoResponseDTO(
                chamado.getId(),
                chamado.getTitulo(),
                chamado.getDescricao(),
                chamado.getStatus(),
                chamado.getPrioridade(),
                usuarioDTO,
                tecnicoDTO
        );
    }

    public ChamadoResponseDTO buscarPorId(Long id) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new ChamadoNaoEncontradoException(id));

        return converterParaDTO(chamado);
    }

}