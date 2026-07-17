package com.alex.helpdesk.dto;


import jakarta.validation.constraints.NotNull;

public record AtribuirTecnicoRequestDTO(
        @NotNull
        Long tecnicoId
) {
}