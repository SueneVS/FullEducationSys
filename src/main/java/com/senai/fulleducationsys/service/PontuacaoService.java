package com.senai.fulleducationsys.service;

import com.senai.fulleducationsys.controller.dto.response.NotasResponse;
import com.senai.fulleducationsys.controller.dto.response.PontuacaoResponse;
import com.senai.fulleducationsys.infra.exception.CustomException.NotFoundException;
import com.senai.fulleducationsys.infra.exception.CustomException.UsuarioNaoAutorizadoException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class PontuacaoService {
    private final NotasService notasService;
    private final TokenService tokenService;

    public PontuacaoResponse getPontuacaoTotal(Long alunoId, String token) {
        String papel = tokenService.buscaCampo(token, "scope");

        if (papel.equals("ALUNO")) {
            Long alunoIdNoToken = Long.valueOf(tokenService.buscaCampo(token, "sub"));
            if (!alunoId.equals(alunoIdNoToken)) {
                throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
            }
        } else if (!papel.equals("ADM") && !papel.equals("PROFESSOR")) {
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        List<NotasResponse> notas = notasService.getNotasPorAluno(alunoId, token);


        if (notas.isEmpty()) {
            log.error("Erro, notas não encontradas");
            throw new NotFoundException("Não há notas cadastradas para o aluno com o ID: " + alunoId);
        }


        double pontuacaoTotal = 0;
        int numeroMaterias = notas.stream().map(NotasResponse::materiaId).distinct().collect(Collectors.toList()).size();

        for (NotasResponse nota : notas) {
            pontuacaoTotal += nota.nota();
        }

        double mediaPontuacao = (pontuacaoTotal / numeroMaterias)*10;

        log.info("Buscando pontuação total por aluno ({}) -> Encontrado", alunoId);

        String mensagem = "Pontuação calculada com sucesso.";
        return new PontuacaoResponse(alunoId, mediaPontuacao);
    }

}
