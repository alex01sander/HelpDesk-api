package com.alex.helpdesk.service;

import com.alex.helpdesk.dto.*;
import com.alex.helpdesk.exception.ChamadoNaoEncontradoException;
import com.alex.helpdesk.exception.TecnicoNaoEncontradoException;
import com.alex.helpdesk.exception.UsuarioNaoEncontradoException;
import com.alex.helpdesk.model.*;
import com.alex.helpdesk.repository.ChamadoRepository;
import com.alex.helpdesk.repository.TecnicoRepository;
import com.alex.helpdesk.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChamadoServiceTest {

    @Mock
    private ChamadoRepository chamadoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TecnicoRepository tecnicoRepository;

    @InjectMocks
    private ChamadoService chamadoService;

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

    @Test
    void deveAbrirChamadoComSucesso() {

        Usuario usuario = criarUsuario();
        ChamadoRequestDTO dto = new ChamadoRequestDTO("Impressora não funciona", "Descrição do problema", Prioridade.ALTA, usuario.getId());

        Chamado chamadoSalvo = new Chamado(dto.titulo(), dto.descricao(), dto.prioridade(), usuario);
        chamadoSalvo.setId(1L);

        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(chamadoRepository.save(any(Chamado.class))).thenReturn(chamadoSalvo);


        ChamadoResponseDTO resultado = chamadoService.abrirChamado(dto);


        assertEquals("Impressora não funciona", resultado.titulo());
        assertEquals(StatusChamado.ABERTO, resultado.status());
        assertEquals("Alex Sander Britto", resultado.usuario().nome());
    }

    @Test
    void deveLancarExcecaoAoAbrirChamadoComUsuarioInexistente() {

        Long usuarioIdInexistente = 999L;
        ChamadoRequestDTO dto = new ChamadoRequestDTO("Título", "Descrição", Prioridade.BAIXA, usuarioIdInexistente);

        when(usuarioRepository.findById(usuarioIdInexistente)).thenReturn(Optional.empty());


        assertThrows(UsuarioNaoEncontradoException.class, () -> {
            chamadoService.abrirChamado(dto);
        });
    }

    @Test
    void deveBuscarChamadoPorIdComSucesso() {

        Usuario usuario = criarUsuario();
        Chamado chamado = new Chamado("Título", "Descrição", Prioridade.MEDIA, usuario);
        chamado.setId(1L);

        when(chamadoRepository.findById(1L)).thenReturn(Optional.of(chamado));


        ChamadoResponseDTO resultado = chamadoService.buscarPorId(1L);


        assertEquals(1L, resultado.id());
        assertEquals(Prioridade.MEDIA, resultado.prioridade());
    }

    @Test
    void deveLancarExcecaoQuandoChamadoNaoExiste() {

        Long idInexistente = 999L;
        when(chamadoRepository.findById(idInexistente)).thenReturn(Optional.empty());


        assertThrows(ChamadoNaoEncontradoException.class, () -> {
            chamadoService.buscarPorId(idInexistente);
        });
    }

    @Test
    void deveAtribuirTecnicoComSucesso() {

        Usuario usuario = criarUsuario();
        Tecnico tecnico = criarTecnico();
        Chamado chamado = new Chamado("Título", "Descrição", Prioridade.ALTA, usuario);
        chamado.setId(1L);

        AtribuirTecnicoRequestDTO dto = new AtribuirTecnicoRequestDTO(tecnico.getId());

        when(chamadoRepository.findById(1L)).thenReturn(Optional.of(chamado));
        when(tecnicoRepository.findById(tecnico.getId())).thenReturn(Optional.of(tecnico));
        when(chamadoRepository.save(any(Chamado.class))).thenReturn(chamado);


        ChamadoResponseDTO resultado = chamadoService.atribuirTecnico(1L, dto);


        assertEquals(StatusChamado.EM_ANDAMENTO, resultado.status());
        assertEquals("Alex Britto", resultado.tecnico().nome());
    }

    @Test
    void deveLancarExcecaoAoAtribuirTecnicoComChamadoInexistente() {

        Long chamadoIdInexistente = 999L;
        AtribuirTecnicoRequestDTO dto = new AtribuirTecnicoRequestDTO(1L);

        when(chamadoRepository.findById(chamadoIdInexistente)).thenReturn(Optional.empty());


        assertThrows(ChamadoNaoEncontradoException.class, () -> {
            chamadoService.atribuirTecnico(chamadoIdInexistente, dto);
        });
    }

    @Test
    void deveLancarExcecaoAoAtribuirTecnicoInexistente() {

        Usuario usuario = criarUsuario();
        Chamado chamado = new Chamado("Título", "Descrição", Prioridade.ALTA, usuario);
        chamado.setId(1L);

        Long tecnicoIdInexistente = 999L;
        AtribuirTecnicoRequestDTO dto = new AtribuirTecnicoRequestDTO(tecnicoIdInexistente);

        when(chamadoRepository.findById(1L)).thenReturn(Optional.of(chamado));
        when(tecnicoRepository.findById(tecnicoIdInexistente)).thenReturn(Optional.empty());


        assertThrows(TecnicoNaoEncontradoException.class, () -> {
            chamadoService.atribuirTecnico(1L, dto);
        });
    }

    @Test
    void deveAtualizarStatusComSucesso() {

        Usuario usuario = criarUsuario();
        Chamado chamado = new Chamado("Título", "Descrição", Prioridade.ALTA, usuario);
        chamado.setId(1L);

        AtualizarStatusRequestDTO dto = new AtualizarStatusRequestDTO(StatusChamado.RESOLVIDO);

        when(chamadoRepository.findById(1L)).thenReturn(Optional.of(chamado));
        when(chamadoRepository.save(any(Chamado.class))).thenReturn(chamado);


        ChamadoResponseDTO resultado = chamadoService.atualizarStatus(1L, dto);


        assertEquals(StatusChamado.RESOLVIDO, resultado.status());
    }

    @Test
    void deveLancarExcecaoAoAtualizarStatusDeChamadoInexistente() {

        Long idInexistente = 999L;
        AtualizarStatusRequestDTO dto = new AtualizarStatusRequestDTO(StatusChamado.FECHADO);

        when(chamadoRepository.findById(idInexistente)).thenReturn(Optional.empty());


        assertThrows(ChamadoNaoEncontradoException.class, () -> {
            chamadoService.atualizarStatus(idInexistente, dto);
        });
    }
}