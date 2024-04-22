package com.senai.fulleducationsys.infra.exception.CustomException;

public class CadastroDuplicadoException extends RuntimeException {

    public CadastroDuplicadoException() {
    }

    public CadastroDuplicadoException(String message) {
        super(message);
    }
}
