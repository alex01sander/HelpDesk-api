package com.alex.helpdesk.exception;

public class TecnicoNaoEncontradoException extends RuntimeException {

    public TecnicoNaoEncontradoException(Long id) {
        super("Técnico não encontrado com o id: " + id);
    }
}