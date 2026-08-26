package com.alex.helpdesk.dto;

import com.alex.helpdesk.model.Especialidade;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TecnicoRequestDTO(
        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String senha,

        @NotNull
        Especialidade especialidade
) {
}