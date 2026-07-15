package com.alex.helpdesk.dto;

import com.alex.helpdesk.model.Especialidade;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TecnicoRequestDTO(
        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        @NotNull
        Especialidade especialidade
) {
}