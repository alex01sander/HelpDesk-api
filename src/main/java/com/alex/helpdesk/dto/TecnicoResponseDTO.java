package com.alex.helpdesk.dto;

import com.alex.helpdesk.model.Especialidade;

public record TecnicoResponseDTO(
        Long id,
        String nome,
        String email,
        Especialidade especialidade
) {
}