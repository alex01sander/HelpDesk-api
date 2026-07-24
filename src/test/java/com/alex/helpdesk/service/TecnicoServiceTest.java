package com.alex.helpdesk.service;

import com.alex.helpdesk.dto.TecnicoRequestDTO;
import com.alex.helpdesk.dto.TecnicoResponseDTO;
import com.alex.helpdesk.exception.TecnicoNaoEncontradoException;
import com.alex.helpdesk.exception.TecnicoPossuiVinculosException;
import com.alex.helpdesk.model.Especialidade;
import com.alex.helpdesk.model.Tecnico;
import com.alex.helpdesk.repository.ChamadoRepository;
import com.alex.helpdesk.repository.ComentarioRepository;
import com.alex.helpdesk.repository.TecnicoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TecnicoServiceTest {

    @Mock
    private TecnicoRepository tecnicoRepository;

    @Mock
    private ChamadoRepository chamadoRepository;

    @Mock
    private ComentarioRepository comentarioRepository;

    @InjectMocks
    private TecnicoService tecnicoService;

    @Test
    void deveCadastrarTecnicoComSucesso() {

        TecnicoRequestDTO dto = new TecnicoRequestDTO("Alex Britto", "alex@helpdesk.com", Especialidade.HARDWARE);
        Tecnico tecnicoSalvo = new Tecnico(dto);
        tecnicoSalvo.setId(1L);

        when(tecnicoRepository.save(any(Tecnico.class))).thenReturn(tecnicoSalvo);


        TecnicoResponseDTO resultado = tecnicoService.cadastrarTecnico(dto);


        assertEquals(1L, resultado.id());
        assertEquals("Alex Britto", resultado.nome());
        assertEquals(Especialidade.HARDWARE, resultado.especialidade());
    }

    @Test
    void deveBuscarTecnicoPorIdComSucesso() {

        Long id = 1L;
        Tecnico tecnico = new Tecnico();
        tecnico.setId(id);
        tecnico.setNome("Alex Britto");
        tecnico.setEmail("alex@helpdesk.com");
        tecnico.setEspecialidade(Especialidade.HARDWARE);

        when(tecnicoRepository.findById(id)).thenReturn(Optional.of(tecnico));


        TecnicoResponseDTO resultado = tecnicoService.buscarPorId(id);


        assertEquals("Alex Britto", resultado.nome());
    }

    @Test
    void deveLancarExcecaoQuandoTecnicoNaoExiste() {

        Long idInexistente = 999L;
        when(tecnicoRepository.findById(idInexistente)).thenReturn(Optional.empty());


        assertThrows(TecnicoNaoEncontradoException.class, () -> {
            tecnicoService.buscarPorId(idInexistente);
        });
    }

    @Test
    void deveAtualizarTecnicoComSucesso() {

        Long id = 1L;
        TecnicoRequestDTO dto = new TecnicoRequestDTO("Alex Sander Britto", "alexnovo@helpdesk.com", Especialidade.REDE);

        Tecnico tecnicoExistente = new Tecnico();
        tecnicoExistente.setId(id);
        tecnicoExistente.setNome("Alex Britto");
        tecnicoExistente.setEmail("alex@helpdesk.com");
        tecnicoExistente.setEspecialidade(Especialidade.HARDWARE);

        when(tecnicoRepository.findById(id)).thenReturn(Optional.of(tecnicoExistente));
        when(tecnicoRepository.save(any(Tecnico.class))).thenReturn(tecnicoExistente);


        TecnicoResponseDTO resultado = tecnicoService.atualizarTecnico(id, dto);


        assertEquals("Alex Sander Britto", resultado.nome());
        assertEquals(Especialidade.REDE, resultado.especialidade());
    }

    @Test
    void deveExcluirTecnicoComSucesso() {

        Long id = 1L;
        Tecnico tecnico = new Tecnico();
        tecnico.setId(id);

        when(tecnicoRepository.findById(id)).thenReturn(Optional.of(tecnico));
        when(chamadoRepository.existsByTecnicoId(id)).thenReturn(false);
        when(comentarioRepository.existsByAutorTecnicoId(id)).thenReturn(false);


        tecnicoService.excluirTecnico(id);


        verify(tecnicoRepository, times(1)).delete(tecnico);
    }

    @Test
    void deveLancarExcecaoAoExcluirTecnicoInexistente() {

        Long idInexistente = 999L;
        when(tecnicoRepository.findById(idInexistente)).thenReturn(Optional.empty());


        assertThrows(TecnicoNaoEncontradoException.class, () -> {
            tecnicoService.excluirTecnico(idInexistente);
        });
    }

    @Test
    void deveLancarExcecaoAoExcluirTecnicoComChamadosVinculados() {

        Long id = 1L;
        Tecnico tecnico = new Tecnico();
        tecnico.setId(id);

        when(tecnicoRepository.findById(id)).thenReturn(Optional.of(tecnico));
        when(chamadoRepository.existsByTecnicoId(id)).thenReturn(true);


        assertThrows(TecnicoPossuiVinculosException.class, () -> {
            tecnicoService.excluirTecnico(id);
        });
    }
}