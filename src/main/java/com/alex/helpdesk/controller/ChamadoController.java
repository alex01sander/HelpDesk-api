package com.alex.helpdesk.controller;

import com.alex.helpdesk.dto.AtribuirTecnicoRequestDTO;
import com.alex.helpdesk.dto.ChamadoRequestDTO;
import com.alex.helpdesk.dto.ChamadoResponseDTO;
import com.alex.helpdesk.service.ChamadoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<ChamadoResponseDTO> listarChamados() {
        return chamadoService.listarChamados();
    }

    @GetMapping("/{id}")
    public ChamadoResponseDTO buscarPorId(@PathVariable Long id) {
        return chamadoService.buscarPorId(id);
    }

    @PatchMapping("/{id}/atribuir-tecnico")
    public ChamadoResponseDTO atribuirTecnico(@PathVariable Long id, @RequestBody @Valid AtribuirTecnicoRequestDTO dto) {
        return chamadoService.atribuirTecnico(id, dto);
    }
}