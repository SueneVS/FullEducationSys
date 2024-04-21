package com.senai.fulleducationsys.controller;

import com.senai.fulleducationsys.service.PontuacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("pontuacao")
public class PontuacaoController {
    private final PontuacaoService pontuacaoService;
    @GetMapping("/alunos/{id_aluno}/pontuacao")
    public ResponseEntity<Double> getPontuacaoTotal(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("id_aluno") Long alunoId) {

        double pontuacao = pontuacaoService.getPontuacaoTotal(alunoId, token.substring(7));

        return ResponseEntity.ok(pontuacao);
    }
}

