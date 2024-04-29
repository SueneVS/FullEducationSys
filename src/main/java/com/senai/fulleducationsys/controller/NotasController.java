package com.senai.fulleducationsys.controller;

import com.senai.fulleducationsys.controller.dto.request.NotasRequest;
import com.senai.fulleducationsys.controller.dto.response.NotasResponse;
import com.senai.fulleducationsys.service.NotasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("notas")


public class NotasController {
    private final NotasService notasService;

    @PostMapping()
    public ResponseEntity<NotasResponse> create(
            @Validated @RequestBody NotasRequest notasRequest,
            @RequestHeader(name = "Authorization") String token) {
        NotasResponse response = notasService.create(notasRequest, token.substring(7));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotasResponse> getEntityId(
            @PathVariable Long id,
            @RequestHeader (name = "Authorization") String token) {
        NotasResponse notasId = notasService.getEntityId(id, token.substring(7));

        return ResponseEntity.ok(notasId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotasResponse> update(
            @PathVariable Long id,
            @Validated @RequestBody NotasRequest notasRequest,
            @RequestHeader (name = "Authorization") String token) {

        NotasResponse notasUpdate = notasService.update(id, notasRequest, token.substring(7));

        return ResponseEntity.ok(notasUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<NotasResponse>> delete(
            @PathVariable Long id,
            @RequestHeader (name = "Authorization") String token) {

        List<NotasResponse> notasDelete = notasService.delete(id, token.substring(7));

        return ResponseEntity.ok(notasDelete);
    }

    @GetMapping("/alunos/{id_aluno}/notas")
    public ResponseEntity<List<NotasResponse>> getNotasPorAluno(
            @RequestHeader (name = "Authorization") String token, @PathVariable("id_aluno") Long alunoId) {

        List<NotasResponse> notas = notasService.getNotasPorAluno(alunoId, token.substring(7));

        return ResponseEntity.ok(notas);
    }
}
