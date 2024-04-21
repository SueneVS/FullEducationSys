package com.senai.fulleducationsys.controller;


import com.senai.fulleducationsys.controller.dto.request.TurmaRequest;
import com.senai.fulleducationsys.controller.dto.response.TurmaResponse;

import com.senai.fulleducationsys.service.TurmaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("turmas")
public class TurmaController {
    private final TurmaService turmaService;

    @PostMapping()
    public ResponseEntity<TurmaResponse> create(
            @Validated @RequestBody TurmaRequest turmaRequest,
            @RequestHeader(name = "Authorization") String token) {
       TurmaResponse response = turmaService.create(turmaRequest, token.substring(7));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurmaResponse> getEntityId(
            @PathVariable Long id,
            @RequestHeader (name = "Authorization") String token) {
        TurmaResponse turmaId = turmaService.getEntityIdDto(id, token.substring(7));

        return ResponseEntity.ok(turmaId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TurmaResponse> update(
            @PathVariable Long id,
            @Validated @RequestBody TurmaRequest turmaRequest,
            @RequestHeader (name = "Authorization") String token) {

        TurmaResponse turmaUpdate = turmaService.update(id, turmaRequest, token.substring(7));

        return ResponseEntity.ok(turmaUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<TurmaResponse>> delete(
            @PathVariable Long id,
            @RequestHeader (name = "Authorization") String token) {

        List<TurmaResponse> turmaDelete = turmaService.delete(id, token.substring(7));

        return ResponseEntity.ok(turmaDelete);
    }

    @GetMapping()
    public ResponseEntity<List<TurmaResponse>> getEntity(
            @RequestHeader (name = "Authorization") String token) {

        List<TurmaResponse> turmas = turmaService.getEntityDto(token.substring(7));

        return ResponseEntity.ok(turmas);
    }
}
