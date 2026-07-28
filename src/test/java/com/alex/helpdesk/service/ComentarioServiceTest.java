package com.alex.helpdesk.service;

import com.alex.helpdesk.dto.ComentarioRequestDTO;
import com.alex.helpdesk.dto.ComentarioResponseDTO;
import com.alex.helpdesk.exception.AutorComentarioInvalidoException;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    @InjectMocks
    private ComentarioService comentarioService;

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Alex Sander Britto");
        usuario.setEmail("alexsander@email.com.br");
        return usuario;
    }

    private Tecnico criarTecnico() {
        Tecnico tecnico = new Tecnico();
        tecnico.setId(1L);
        tecnico.setNome("Alex Britto");
        tecnico.setEmail("alex@helpdesk.com");
        tecnico.setEspecialidade(Especialidade.HARDWARE);
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
        ComentarioRequestDTO dto = new ComentarioRequestDTO("Já testei, ainda não funciona", usuario.getId(), null);

        Comentario comentarioSalvo = new Comentario(dto.texto(), chamado, usuario, null);

        when(chamadoRepository.findById(chamado.getId())).thenReturn(Optional.of(chamado));
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(comentarioRepository.save(any(Comentario.class))).thenReturn(comentarioSalvo);

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
        ComentarioRequestDTO dto = new ComentarioRequestDTO("Verificando o problema", null, tecnico.getId());

        Comentario comentarioSalvo = new Comentario(dto.texto(), chamado, null, tecnico);

        when(chamadoRepository.findById(chamado.getId())).thenReturn(Optional.of(chamado));
        when(tecnicoRepository.findById(tecnico.getId())).thenReturn(Optional.of(tecnico));
        when(comentarioRepository.save(any(Comentario.class))).thenReturn(comentarioSalvo);

        // Act
        ComentarioResponseDTO resultado = comentarioService.adicionarComentario(chamado.getId(), dto);

        // Assert
        assertEquals("TECNICO", resultado.tipoAutor());
        assertEquals("Alex Britto", resultado.autorNome());
    }

    @Test
    void deveLancarExcecaoQuandoAutorTemUsuarioETecnico() {
        // Arrange
        Usuario usuario = criarUsuario();
        Chamado chamado = criarChamado(usuario);
        ComentarioRequestDTO dto = new ComentarioRequestDTO("Texto", 1L, 1L);

        when(chamadoRepository.findById(chamado.getId())).thenReturn(Optional.of(chamado));

        // Act + Assert
        assertThrows(AutorComentarioInvalidoException.class, () -> {
            comentarioService.adicionarComentario(chamado.getId(), dto);
        });
    }

    @Test
    void deveLancarExcecaoQuandoAutorNaoTemUsuarioNemTecnico() {
        // Arrange
        Usuario usuario = criarUsuario();
        Chamado chamado = criarChamado(usuario);
        ComentarioRequestDTO dto = new ComentarioRequestDTO("Texto", null, null);

        when(chamadoRepository.findById(chamado.getId())).thenReturn(Optional.of(chamado));

        // Act + Assert
        assertThrows(AutorComentarioInvalidoException.class, () -> {
            comentarioService.adicionarComentario(chamado.getId(), dto);
        });
    }

    @Test
    void deveLancarExcecaoAoComentarEmChamadoInexistente() {
        // Arrange
        Long chamadoIdInexistente = 999L;
        ComentarioRequestDTO dto = new ComentarioRequestDTO("Texto", 1L, null);

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