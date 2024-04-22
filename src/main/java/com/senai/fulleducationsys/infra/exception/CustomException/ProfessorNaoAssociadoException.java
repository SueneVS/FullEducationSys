package com.senai.fulleducationsys.infra.exception.CustomException;

public class ProfessorNaoAssociadoException extends RuntimeException{

    public ProfessorNaoAssociadoException() {
    }

    public ProfessorNaoAssociadoException(String message) {
        super(message);
    }
}
