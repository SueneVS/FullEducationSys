package com.senai.fulleducationsys.infra.exception.CustomException;

public class UsuarioNaoAutorizadoException extends RuntimeException {
    public UsuarioNaoAutorizadoException() {
    }
    public UsuarioNaoAutorizadoException(String message) {
        super(message);
    }
}
