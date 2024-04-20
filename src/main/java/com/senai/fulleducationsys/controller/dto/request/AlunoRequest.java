package com.senai.fulleducationsys.controller.dto.request;


import java.util.Date;

public record AlunoRequest(String nome, Date dataNascimento, Long usuarioId, Long turmaId) {
}
