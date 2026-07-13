package com.alex.helpdesk.controller;

import com.alex.helpdesk.dto.UsuarioRequestDTO;
import com.alex.helpdesk.dto.UsuarioResponseDTO;
import com.alex.helpdesk.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{id}")
    public UsuarioResponseDTO buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public UsuarioResponseDTO atualizarUsuario(@PathVariable Long id, @RequestBody @Valid UsuarioRequestDTO dto) {
        return usuarioService.atualizarUsuario(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirUsuario(@PathVariable Long id) {
        usuarioService.excluirUsuario(id);
        return ResponseEntity.noContent().build();
    }
}