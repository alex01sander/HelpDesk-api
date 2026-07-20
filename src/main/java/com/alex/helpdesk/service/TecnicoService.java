package com.alex.helpdesk.service;

import com.alex.helpdesk.dto.TecnicoRequestDTO;
import com.alex.helpdesk.dto.TecnicoResponseDTO;
import com.alex.helpdesk.exception.TecnicoNaoEncontradoException;
import com.alex.helpdesk.exception.TecnicoPossuiVinculosException;
import com.alex.helpdesk.model.Tecnico;
import com.alex.helpdesk.repository.ChamadoRepository;
import com.alex.helpdesk.repository.ComentarioRepository;
import com.alex.helpdesk.repository.TecnicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;
    private final ChamadoRepository chamadoRepository;
    private final ComentarioRepository comentarioRepository;

    public TecnicoService(TecnicoRepository tecnicoRepository,
                          ChamadoRepository chamadoRepository,
                          ComentarioRepository comentarioRepository) {
        this.tecnicoRepository = tecnicoRepository;
        this.chamadoRepository = chamadoRepository;
        this.comentarioRepository = comentarioRepository;
    }

    public TecnicoResponseDTO cadastrarTecnico(TecnicoRequestDTO dto) {
        Tecnico tecnico = new Tecnico(dto);
        Tecnico tecnicoSalvo = tecnicoRepository.save(tecnico);
        return converterParaDTO(tecnicoSalvo);
    }

    public List<TecnicoResponseDTO> listarTecnicos() {
        return tecnicoRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public TecnicoResponseDTO buscarPorId(Long id) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new TecnicoNaoEncontradoException(id));

        return converterParaDTO(tecnico);
    }

    public TecnicoResponseDTO atualizarTecnico(Long id, TecnicoRequestDTO dto) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new TecnicoNaoEncontradoException(id));

        tecnico.setNome(dto.nome());
        tecnico.setEmail(dto.email());
        tecnico.setEspecialidade(dto.especialidade());

        Tecnico tecnicoAtualizado = tecnicoRepository.save(tecnico);

        return converterParaDTO(tecnicoAtualizado);
    }

    public void excluirTecnico(Long id) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new TecnicoNaoEncontradoException(id));

        boolean possuiChamados = chamadoRepository.existsByTecnicoId(id);
        boolean possuiComentarios = comentarioRepository.existsByAutorTecnicoId(id);

        if (possuiChamados || possuiComentarios) {
            throw new TecnicoPossuiVinculosException(id);
        }

        tecnicoRepository.delete(tecnico);
    }

    private TecnicoResponseDTO converterParaDTO(Tecnico tecnico) {
        return new TecnicoResponseDTO(
                tecnico.getId(),
                tecnico.getNome(),
                tecnico.getEmail(),
                tecnico.getEspecialidade()
        );
    }
}