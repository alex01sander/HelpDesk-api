package com.alex.helpdesk.exception;

import java.time.LocalDateTime;

public record ErroResposta(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        String path
) {
}
