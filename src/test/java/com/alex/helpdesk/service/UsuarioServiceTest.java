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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveCadastrarUsuarioComSucesso() {
        // Arrange
        UsuarioRequestDTO dto = new UsuarioRequestDTO("Alex Britto", "alex@email.com", "senha123");
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
        Long idInexistente = 999L;
        when(usuarioRepository.findById(idInexistente)).thenReturn(Optional.empty());
        assertThrows(UsuarioNaoEncontradoException.class, () -> usuarioService.buscarPorId(idInexistente));
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        Long id = 1L;
        UsuarioRequestDTO dto = new UsuarioRequestDTO("Alex Sander Britto", "alexnovo@email.com", "senha123");

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);
        usuarioExistente.setNome("Alex Britto");
        usuarioExistente.setEmail("alex@email.com");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(org.mockito.ArgumentMatchers.any(Usuario.class)))
                .thenReturn(usuarioExistente);

        UsuarioResponseDTO resultado = usuarioService.atualizarUsuario(id, dto);

        assertEquals("Alex Sander Britto", resultado.nome());
        assertEquals("alexnovo@email.com", resultado.email());
    }

    @Test
    void deveExcluirUsuarioComSucesso() {
        Long id = 1L;
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);
        usuarioExistente.setNome("Alex Britto");
        usuarioExistente.setEmail("alex@email.com");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));


        usuarioService.excluirUsuario(id);


        org.mockito.Mockito.verify(usuarioRepository, org.mockito.Mockito.times(1)).delete(usuarioExistente);
    }

    @Test
    void deveLancarExcecaoAoExcluirUsuarioInexistente() {
        Long idInexistente = 999L;
        when(usuarioRepository.findById(idInexistente)).thenReturn(Optional.empty());
        assertThrows(UsuarioNaoEncontradoException.class, () -> usuarioService.excluirUsuario(idInexistente));
    }
}