package com.senai.fulleducationsys.controller.dto.request;

import java.time.LocalDate;

public record NotasRequest(Long alunoId, Long professorId, Long materiaId, Double nota, LocalDate dataNota) {
}
