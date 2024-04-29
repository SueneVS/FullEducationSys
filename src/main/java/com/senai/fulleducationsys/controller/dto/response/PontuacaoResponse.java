package com.senai.fulleducationsys.controller.dto.response;

public record PontuacaoResponse(Long alunoId, double mediaPontuacao) {

    public void setMensagem(String pontuaçãoCalculadaComSucesso) {
    }
}
