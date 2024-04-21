package com.senai.fulleducationsys.service;

import com.senai.fulleducationsys.controller.dto.response.NotasResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class PontuacaoService {
    private final NotasService notasService;
    private final TokenService tokenService;
    public double getPontuacaoTotal(Long alunoId, String token) {

        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO") && !papel.equals("RECRUITER")){
            throw new RuntimeException("Acesso não autorizado.");
        }

        List<Double> notas = notasService.getNotasPorAluno(alunoId, token);

        if (notas.isEmpty()) {
            throw new RuntimeException("Não há notas cadastradas para o aluno com o ID: " + alunoId);
        }


        double pontuacaoTotal = 0;
        for (Double nota : notas){
            pontuacaoTotal += nota;
        }

        return pontuacaoTotal/ notas.size();
    }


}
