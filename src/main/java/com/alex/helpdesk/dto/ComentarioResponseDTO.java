package com.alex.helpdesk.dto;

import java.time.LocalDateTime;

public record ComentarioResponseDTO(
        Long id,
        String texto,
        LocalDateTime dataCriacao,
        String autorNome,
        String tipoAutor
) {
}