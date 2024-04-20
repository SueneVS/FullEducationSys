package com.senai.fulleducationsys.controller.dto.request;

import java.time.LocalDate;

public record DocenteRequest(String nome, LocalDate dataEntrada, Long usuarioId) {
}
