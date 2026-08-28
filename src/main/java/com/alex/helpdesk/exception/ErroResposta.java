package com.alex.helpdesk.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErroResposta(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        String path,
        List<String> campos
) {
    public ErroResposta(LocalDateTime timestamp, int status, String erro, String mensagem, String path) {
        this(timestamp, status, erro, mensagem, path, null);
    }
}
