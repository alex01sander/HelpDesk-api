package com.alex.helpdesk.dto;

import com.alex.helpdesk.model.Prioridade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChamadoRequestDTO(
        @NotBlank
        String titulo,

        @NotBlank
        String descricao,

        @NotNull
        Prioridade prioridade,

        @NotNull
        Long usuarioId
) {
}