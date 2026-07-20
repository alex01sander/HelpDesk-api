package com.alex.helpdesk.controller;

import com.alex.helpdesk.dto.TecnicoRequestDTO;
import com.alex.helpdesk.dto.TecnicoResponseDTO;
import com.alex.helpdesk.service.TecnicoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public TecnicoResponseDTO buscarPorId(@PathVariable Long id) {
        return tecnicoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public TecnicoResponseDTO atualizarTecnico(@PathVariable Long id, @RequestBody @Valid TecnicoRequestDTO dto) {
        return tecnicoService.atualizarTecnico(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirTecnico(@PathVariable Long id) {
        tecnicoService.excluirTecnico(id);
        return ResponseEntity.noContent().build();
    }
}