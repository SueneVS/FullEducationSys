package com.senai.fulleducationsys.infra.exception.CustomException;

public class CampoObrigatorioException extends RuntimeException {

    public CampoObrigatorioException() {
    }
    public CampoObrigatorioException(String message) {
        super(message);
    }
}
