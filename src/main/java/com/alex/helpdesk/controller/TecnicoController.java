package com.alex.helpdesk.controller;

import com.alex.helpdesk.dto.TecnicoRequestDTO;
import com.alex.helpdesk.dto.TecnicoResponseDTO;
import com.alex.helpdesk.service.TecnicoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tecnicos")
public class TecnicoController {

    private final TecnicoService tecnicoService;

    public TecnicoController(TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    @PostMapping
    public TecnicoResponseDTO cadastrarTecnico(@RequestBody @Valid TecnicoRequestDTO dto) {
        return tecnicoService.cadastrarTecnico(dto);
    }

    @GetMapping
    public List<TecnicoResponseDTO> listarTecnicos() {
        return tecnicoService.listarTecnicos();
    }
}