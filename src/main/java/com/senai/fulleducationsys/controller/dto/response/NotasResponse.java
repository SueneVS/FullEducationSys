package com.senai.fulleducationsys.controller.dto.response;

import java.time.LocalDate;

public record NotasResponse(Long notasId, String alunoId, String professorId, String materiaId, Double nota, LocalDate dataNota) {


}
