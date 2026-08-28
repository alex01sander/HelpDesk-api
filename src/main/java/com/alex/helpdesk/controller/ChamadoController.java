package com.alex.helpdesk.controller;

import com.alex.helpdesk.dto.AtribuirTecnicoRequestDTO;
import com.alex.helpdesk.dto.AtualizarStatusRequestDTO;
import com.alex.helpdesk.dto.ChamadoRequestDTO;
import com.alex.helpdesk.dto.ChamadoResponseDTO;
import com.alex.helpdesk.service.ChamadoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;

    public ChamadoController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    @PostMapping
    public ChamadoResponseDTO abrirChamado(@RequestBody @Valid ChamadoRequestDTO dto) {
        return chamadoService.abrirChamado(dto);
    }

    @GetMapping
    public Page<ChamadoResponseDTO> listarChamados(Pageable pageable) {
        return chamadoService.listarChamados(pageable);
    }

    @GetMapping("/{id}")
    public ChamadoResponseDTO buscarPorId(@PathVariable Long id) {
        return chamadoService.buscarPorId(id);
    }

    @PatchMapping("/{id}/atribuir-tecnico")
    public ChamadoResponseDTO atribuirTecnico(@PathVariable Long id, @RequestBody @Valid AtribuirTecnicoRequestDTO dto) {
        return chamadoService.atribuirTecnico(id, dto);
    }

    @PatchMapping("/{id}/status")
    public ChamadoResponseDTO atualizarStatus(@PathVariable Long id, @RequestBody @Valid AtualizarStatusRequestDTO dto) {
        return chamadoService.atualizarStatus(id, dto);
    }
}