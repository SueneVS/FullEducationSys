package com.senai.fulleducationsys.infra.exception;

import com.senai.fulleducationsys.controller.dto.Erro;
import com.senai.fulleducationsys.infra.exception.CustomException.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@ControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handler(Exception e) {
        log.error("STATUS: 500 -> Erro inesperado -> {}, {}", e.getMessage(), e.getCause().getStackTrace());
        Erro erro = Erro.builder()
                .codigo("500")
                .mensagem(e.getMessage())
                .build();
        return ResponseEntity.status(500).body(erro);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handler(NotFoundException e) {
        Erro erro = Erro.builder()
                .codigo("404")
                .mensagem(e.getMessage())
                .build();
        log.error("STATUS: 404 -> Recurso não encontrado -> {}", e.getMessage());
        return ResponseEntity.status(404).body(erro);
    }

    @ExceptionHandler(CampoObrigatorioException.class)
    public ResponseEntity<?> handle(CampoObrigatorioException e) {
        Erro erro = Erro.builder()
                .codigo("400")
                .mensagem(e.getMessage())
                .build();
        log.error("STATUS: 400 -> Requisição inválida. Dados ausentes ou incorretos. -> {}", e.getMessage());
        return ResponseEntity.status(400).body(erro);
    }

    @ExceptionHandler(CadastroDuplicadoException.class)
    public ResponseEntity<?> handle(CadastroDuplicadoException e) {
        log.error("STATUS: 409 -> Cadastro duplicado -> {}", e.getMessage());
        Erro erro = Erro.builder()
                .codigo("409")
                .mensagem(e.getMessage())
                .build();
        return ResponseEntity.status(409).body(erro);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handler(DataIntegrityViolationException e) {
        log.error("STATUS: 400 -> Violação de integridade -> {}", e.getMessage());
        Erro erro = Erro.builder()
                .codigo("400")
                .mensagem(e.getMessage())
                .build();
        return ResponseEntity.status(400).body(erro);
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handler(IllegalStateException e) {
        log.error("STATUS: 409 -> Erro ao gerar a solicitação -> {}", e.getMessage());
        Erro erro = Erro.builder()
                .codigo("409")
                .mensagem(e.getMessage())
                .build();
        return ResponseEntity.status(409).body(erro);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<?> handler(UnsupportedOperationException e) {
        log.error("STATUS: 403 -> Operação não aceita -> {}", e.getMessage());
        Erro erro = Erro.builder()
                .codigo("403")
                .mensagem(e.getMessage())
                .build();
        return ResponseEntity.status(403).body(erro);
    }

    @ExceptionHandler(UsuarioNaoAutorizadoException.class)
    public ResponseEntity<?> handler(UsuarioNaoAutorizadoException e) {
        log.error("STATUS: 401 -> Credenciais inválidas. O usuário não está autorizado a acessar o sistema -> {}", e.getMessage());
        Erro erro = Erro.builder()
                .codigo("401")
                .mensagem(e.getMessage())
                .build();
        return ResponseEntity.status(401).body(erro);
    }




}
