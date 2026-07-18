package com.alex.helpdesk.dto;

import jakarta.validation.constraints.NotBlank;

public record ComentarioRequestDTO(
        @NotBlank
        String texto,

        Long usuarioId,

        Long tecnicoId
) {
}