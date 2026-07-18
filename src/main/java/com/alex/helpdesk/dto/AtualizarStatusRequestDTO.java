package com.alex.helpdesk.dto;

import com.alex.helpdesk.model.StatusChamado;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusRequestDTO(
        @NotNull
        StatusChamado status
) {
}