package com.senai.fulleducationsys.controller.dto.request;

import java.time.LocalDate;

public record TurmaRequest(String nome, Long professorId,Long cursoId) {
}
