package com.senai.fulleducationsys.controller;

import com.senai.fulleducationsys.controller.dto.request.CursoRequest;
import com.senai.fulleducationsys.controller.dto.response.CursoResponse;
import com.senai.fulleducationsys.service.CursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("cursos")

public class CursoController {

    private final CursoService cursoService;

    @PostMapping()
    public ResponseEntity<String> create(
            @Validated @RequestBody CursoRequest cursoRequest,
            @RequestHeader (name = "Authorization") String token) {
        cursoService.create(cursoRequest, token.substring(7));
        return ResponseEntity.ok("Curso Salvo!");
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<CursoResponse> getEntityId(
            @PathVariable Long id,
            @RequestHeader (name = "Authorization") String token) {
       CursoResponse cursoId = cursoService.getEntityIdDto(id, token.substring(7));

        return ResponseEntity.ok(cursoId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoResponse> update(
            @PathVariable Long id,
            @Validated @RequestBody CursoRequest cursoRequest,
            @RequestHeader (name = "Authorization") String token) {

        CursoResponse cursoUpdate = cursoService.update(id, cursoRequest, token.substring(7));

        return ResponseEntity.ok(cursoUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<CursoResponse>> delete(
            @PathVariable Long id,
            @RequestHeader (name = "Authorization") String token) {

        List<CursoResponse> cursoDelete = cursoService.delete(id, token.substring(7));

        return ResponseEntity.ok(cursoDelete);
    }

    @GetMapping()
    public ResponseEntity<List<CursoResponse>> getEntity(
            @RequestHeader (name = "Authorization") String token) {

        List<CursoResponse> cursos = cursoService.getEntityDto(token.substring(7));

        return ResponseEntity.ok(cursos);
    }

}
