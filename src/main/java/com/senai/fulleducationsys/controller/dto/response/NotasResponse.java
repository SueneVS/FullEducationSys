package com.senai.fulleducationsys.controller.dto.response;

import java.time.LocalDate;

public record NotasResponse(Long notasId, Long alunoId, Long professorId, Long materiaId, Double nota, LocalDate dataNota) {

}
