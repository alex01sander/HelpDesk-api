package com.alex.helpdesk.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException(Long id) {
        super("Usuário não encontrado com o id: " + id);
    }

    public UsuarioNaoEncontradoException(String email) {
        super("Usuário não encontrado com o email: " + email);
    }
}
