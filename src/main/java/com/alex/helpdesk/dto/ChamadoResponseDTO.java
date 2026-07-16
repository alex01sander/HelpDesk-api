package com.alex.helpdesk.dto;

import com.alex.helpdesk.model.Prioridade;
import com.alex.helpdesk.model.StatusChamado;

public record ChamadoResponseDTO(
        Long id,
        String titulo,
        String descricao,
        StatusChamado status,
        Prioridade prioridade,
        UsuarioResponseDTO usuario,
        TecnicoResponseDTO tecnico
) {
}