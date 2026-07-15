package com.alex.helpdesk.service;

import com.alex.helpdesk.dto.TecnicoRequestDTO;
import com.alex.helpdesk.dto.TecnicoResponseDTO;
import com.alex.helpdesk.model.Tecnico;
import com.alex.helpdesk.repository.TecnicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;

    public TecnicoService(TecnicoRepository tecnicoRepository) {
        this.tecnicoRepository = tecnicoRepository;
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

    private TecnicoResponseDTO converterParaDTO(Tecnico tecnico) {
        return new TecnicoResponseDTO(
                tecnico.getId(),
                tecnico.getNome(),
                tecnico.getEmail(),
                tecnico.getEspecialidade()
        );
    }
}