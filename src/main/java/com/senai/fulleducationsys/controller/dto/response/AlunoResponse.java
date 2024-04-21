package com.senai.fulleducationsys.controller.dto.response;
import java.util.Date;

public record AlunoResponse(Long alunoId, String nome, Date dataNascimento, Long usuarioId, String turmaId) {
}
