package com.alex.helpdesk.service;

import com.alex.helpdesk.dto.ComentarioRequestDTO;
import com.alex.helpdesk.dto.ComentarioResponseDTO;
import com.alex.helpdesk.exception.ChamadoNaoEncontradoException;
import com.alex.helpdesk.model.*;
import com.alex.helpdesk.repository.ChamadoRepository;
import com.alex.helpdesk.repository.ComentarioRepository;
import com.alex.helpdesk.repository.TecnicoRepository;
import com.alex.helpdesk.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComentarioServiceTest {

    @Mock
    private ComentarioRepository comentarioRepository;

    @Mock
    private ChamadoRepository chamadoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TecnicoRepository tecnicoRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ComentarioService comentarioService;

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Alex Sander Britto");
        usuario.setEmail("alexsander@email.com.br");
        usuario.setRole(Role.USUARIO);
        return usuario;
    }

    private Tecnico criarTecnico() {
        Tecnico tecnico = new Tecnico();
        tecnico.setId(1L);
        tecnico.setNome("Alex Britto");
        tecnico.setEmail("alex@helpdesk.com");
        tecnico.setEspecialidade(Especialidade.HARDWARE);
        tecnico.setRole(Role.TECNICO);
        return tecnico;
    }

    private Chamado criarChamado(Usuario usuario) {
        Chamado chamado = new Chamado("Título", "Descrição", Prioridade.ALTA, usuario);
        chamado.setId(1L);
        return chamado;
    }

    @Test
    void deveAdicionarComentarioDeUsuarioComSucesso() {
        // Arrange
        Usuario usuario = criarUsuario();
        Chamado chamado = criarChamado(usuario);
        ComentarioRequestDTO dto = new ComentarioRequestDTO("Já testei, ainda não funciona");

        Comentario comentarioSalvo = new Comentario(dto.texto(), chamado, usuario, null);

        when(chamadoRepository.findById(chamado.getId())).thenReturn(Optional.of(chamado));
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(comentarioRepository.save(any(Comentario.class))).thenReturn(comentarioSalvo);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(usuario.getEmail());
        when(authentication.getAuthorities()).thenReturn((List) List.of(new SimpleGrantedAuthority("ROLE_USUARIO")));

        // Act
        ComentarioResponseDTO resultado = comentarioService.adicionarComentario(chamado.getId(), dto);

        // Assert
        assertEquals("USUARIO", resultado.tipoAutor());
        assertEquals("Alex Sander Britto", resultado.autorNome());
    }

    @Test
    void deveAdicionarComentarioDeTecnicoComSucesso() {
        // Arrange
        Usuario usuario = criarUsuario();
        Tecnico tecnico = criarTecnico();
        Chamado chamado = criarChamado(usuario);
        ComentarioRequestDTO dto = new ComentarioRequestDTO("Verificando o problema");

        Comentario comentarioSalvo = new Comentario(dto.texto(), chamado, null, tecnico);

        when(chamadoRepository.findById(chamado.getId())).thenReturn(Optional.of(chamado));
        when(tecnicoRepository.findByEmail(tecnico.getEmail())).thenReturn(Optional.of(tecnico));
        when(comentarioRepository.save(any(Comentario.class))).thenReturn(comentarioSalvo);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(tecnico.getEmail());
        when(authentication.getAuthorities()).thenReturn((List) List.of(new SimpleGrantedAuthority("ROLE_TECNICO")));

        // Act
        ComentarioResponseDTO resultado = comentarioService.adicionarComentario(chamado.getId(), dto);

        // Assert
        assertEquals("TECNICO", resultado.tipoAutor());
        assertEquals("Alex Britto", resultado.autorNome());
    }

    @Test
    void deveLancarExcecaoAoComentarEmChamadoInexistente() {
        // Arrange
        Long chamadoIdInexistente = 999L;
        ComentarioRequestDTO dto = new ComentarioRequestDTO("Texto");

        when(chamadoRepository.findById(chamadoIdInexistente)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(ChamadoNaoEncontradoException.class, () -> {
            comentarioService.adicionarComentario(chamadoIdInexistente, dto);
        });
    }

    @Test
    void deveListarComentariosComSucesso() {
        // Arrange
        Usuario usuario = criarUsuario();
        Chamado chamado = criarChamado(usuario);
        Comentario comentario = new Comentario("Texto do comentário", chamado, usuario, null);

        when(chamadoRepository.findById(chamado.getId())).thenReturn(Optional.of(chamado));
        when(comentarioRepository.findByChamadoId(chamado.getId())).thenReturn(List.of(comentario));

        // Act
        List<ComentarioResponseDTO> resultado = comentarioService.listarComentarios(chamado.getId());

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("Texto do comentário", resultado.get(0).texto());
    }

    @Test
    void deveLancarExcecaoAoListarComentariosDeChamadoInexistente() {
        // Arrange
        Long chamadoIdInexistente = 999L;
        when(chamadoRepository.findById(chamadoIdInexistente)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(ChamadoNaoEncontradoException.class, () -> {
            comentarioService.listarComentarios(chamadoIdInexistente);
        });
    }
}