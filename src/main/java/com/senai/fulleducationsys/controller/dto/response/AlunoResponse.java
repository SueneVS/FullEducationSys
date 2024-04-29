package com.senai.fulleducationsys.controller.dto.response;
import java.time.LocalDate;


public record AlunoResponse(Long alunoId, String nome, LocalDate dataNascimento, String usuarioId, String turmaId) {
}
