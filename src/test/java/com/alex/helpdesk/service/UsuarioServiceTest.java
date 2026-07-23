package com.alex.helpdesk.service;

import com.alex.helpdesk.dto.UsuarioRequestDTO;
import com.alex.helpdesk.dto.UsuarioResponseDTO;
import com.alex.helpdesk.exception.UsuarioNaoEncontradoException;
import com.alex.helpdesk.model.Usuario;
import com.alex.helpdesk.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveCadastrarUsuarioComSucesso() {
        // Arrange
        UsuarioRequestDTO dto = new UsuarioRequestDTO("Alex Britto", "alex@email.com");
        Usuario usuarioSalvo = new Usuario(dto);
        usuarioSalvo.setId(1L);

        when(usuarioRepository.save(org.mockito.ArgumentMatchers.any(Usuario.class)))
                .thenReturn(usuarioSalvo);

        // Act
        UsuarioResponseDTO resultado = usuarioService.cadastrarUsuario(dto);

        // Assert
        assertEquals(1L, resultado.id());
        assertEquals("Alex Britto", resultado.nome());
        assertEquals("alex@email.com", resultado.email());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        // Arrange
        Long idInexistente = 999L;
        when(usuarioRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(UsuarioNaoEncontradoException.class, () -> {
            usuarioService.buscarPorId(idInexistente);
        });
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        // Arrange
        Long id = 1L;
        UsuarioRequestDTO dto = new UsuarioRequestDTO("Alex Sander Britto", "alexnovo@email.com");

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);
        usuarioExistente.setNome("Alex Britto");
        usuarioExistente.setEmail("alex@email.com");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(org.mockito.ArgumentMatchers.any(Usuario.class)))
                .thenReturn(usuarioExistente);

        // Act
        UsuarioResponseDTO resultado = usuarioService.atualizarUsuario(id, dto);

        // Assert
        assertEquals("Alex Sander Britto", resultado.nome());
        assertEquals("alexnovo@email.com", resultado.email());
    }
}