package com.alex.helpdesk.controller;

import com.alex.helpdesk.dto.UsuarioRequestDTO;
import com.alex.helpdesk.dto.UsuarioResponseDTO;
import com.alex.helpdesk.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public UsuarioResponseDTO cadastrarUsuario(@RequestBody @Valid UsuarioRequestDTO dto) {
        return usuarioService.cadastrarUsuario(dto);
    }
}