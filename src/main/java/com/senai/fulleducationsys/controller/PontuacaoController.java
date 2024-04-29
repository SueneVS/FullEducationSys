package com.senai.fulleducationsys.controller;

import com.senai.fulleducationsys.controller.dto.response.PontuacaoResponse;
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
    public ResponseEntity<PontuacaoResponse> getPontuacaoTotal(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("id_aluno") Long alunoId) {

        PontuacaoResponse pontuacao = pontuacaoService.getPontuacaoTotal(alunoId, token.substring(7));
        pontuacao.setMensagem("Pontuação calculada com Sucesso");
        return ResponseEntity.ok(pontuacao);
    }
}

