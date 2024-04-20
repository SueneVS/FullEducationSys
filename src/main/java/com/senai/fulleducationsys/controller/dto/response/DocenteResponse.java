package com.senai.fulleducationsys.controller.dto.response;

import java.time.LocalDate;

public record DocenteResponse(Long docenteId, String nome, LocalDate dataEntrada, Long usuarioId) {

}
