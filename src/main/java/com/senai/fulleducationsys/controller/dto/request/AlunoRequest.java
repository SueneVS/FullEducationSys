package com.senai.fulleducationsys.controller.dto.request;


import java.time.LocalDate;


public record AlunoRequest(String nome, LocalDate dataNascimento, Long usuarioId, Long turmaId) {
}
