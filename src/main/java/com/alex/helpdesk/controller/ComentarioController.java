package com.alex.helpdesk.controller;

import com.alex.helpdesk.dto.ComentarioRequestDTO;
import com.alex.helpdesk.dto.ComentarioResponseDTO;
import com.alex.helpdesk.service.ComentarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chamados/{chamadoId}/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @PostMapping
    public ComentarioResponseDTO adicionarComentario(@PathVariable Long chamadoId, @RequestBody @Valid ComentarioRequestDTO dto) {
        return comentarioService.adicionarComentario(chamadoId, dto);
    }

    @GetMapping
    public List<ComentarioResponseDTO> listarComentarios(@PathVariable Long chamadoId) {
        return comentarioService.listarComentarios(chamadoId);
    }
}