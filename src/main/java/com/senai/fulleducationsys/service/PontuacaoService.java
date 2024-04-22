package com.senai.fulleducationsys.service;

import com.senai.fulleducationsys.controller.dto.response.NotasResponse;
import com.senai.fulleducationsys.infra.exception.CustomException.NotFoundException;
import com.senai.fulleducationsys.infra.exception.CustomException.UsuarioNaoAutorizadoException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class PontuacaoService {
    private final NotasService notasService;
    private final TokenService tokenService;
    public double getPontuacaoTotal(Long alunoId, String token) {

        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO") && !papel.equals("RECRUITER")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        List<Double> notas = notasService.getNotasPorAluno(alunoId, token);

        if (notas.isEmpty()) {
            log.error("Erro, notas não encontradas");
            throw new NotFoundException("Não há notas cadastradas para o aluno com o ID: " + alunoId);
        }


        double pontuacaoTotal = 0;
        for (Double nota : notas){
            pontuacaoTotal += nota;
        }
        log.info("Buscando pontuação total por aluno ({}) -> Encontrado", alunoId);
        return pontuacaoTotal/ notas.size();
    }


}
