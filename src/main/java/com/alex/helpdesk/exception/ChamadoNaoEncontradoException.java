package com.alex.helpdesk.exception;

public class ChamadoNaoEncontradoException extends RuntimeException {

    public ChamadoNaoEncontradoException(Long id) {
        super("Chamado não encontrado com o id: " + id);
    }
}