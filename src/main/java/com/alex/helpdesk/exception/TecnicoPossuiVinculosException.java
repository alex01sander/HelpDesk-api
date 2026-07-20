package com.alex.helpdesk.exception;

public class TecnicoPossuiVinculosException extends RuntimeException {

    public TecnicoPossuiVinculosException(Long id) {
        super("Não é possível excluir o técnico de id " + id + " pois ele possui chamados ou comentários vinculados.");
    }
}