package com.senai.fulleducationsys.controller;

import com.senai.fulleducationsys.controller.dto.request.AlunoRequest;
import com.senai.fulleducationsys.controller.dto.response.AlunoResponse;
import com.senai.fulleducationsys.service.AlunoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("alunos")
public class AlunoController {
    private final AlunoService alunoService;

    @PostMapping()
    public ResponseEntity<AlunoResponse> create(
            @Validated @RequestBody AlunoRequest alunoRequest,
            @RequestHeader(name = "Authorization") String token) {
        AlunoResponse response = alunoService.create(alunoRequest, token.substring(7));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponse> getEntityId(
            @PathVariable Long id,
            @RequestHeader (name = "Authorization") String token) {
        AlunoResponse alunoId = alunoService.getEntityIdDto(id, token.substring(7));

        return ResponseEntity.ok(alunoId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponse> update(
            @PathVariable Long id,
            @Validated @RequestBody AlunoRequest alunoRequest,
            @RequestHeader (name = "Authorization") String token) {

        AlunoResponse alunoUpdate = alunoService.update(id, alunoRequest, token.substring(7));

        return ResponseEntity.ok(alunoUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<AlunoResponse>> delete(
            @PathVariable Long id,
            @RequestHeader (name = "Authorization") String token) {

        List<AlunoResponse> alunoDelete = alunoService.delete(id, token.substring(7));

        return ResponseEntity.ok(alunoDelete);
    }

    @GetMapping()
    public ResponseEntity<List<AlunoResponse>> getEntity(
            @RequestHeader (name = "Authorization") String token) {

        List<AlunoResponse> alunos = alunoService.getEntityDto(token.substring(7));

        return ResponseEntity.ok(alunos);
    }
}
