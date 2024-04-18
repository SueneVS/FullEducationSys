package com.senai.fulleducationsys.controller;

import com.senai.fulleducationsys.controller.dto.request.CursoRequest;
import com.senai.fulleducationsys.controller.dto.request.MateriaRequest;

import com.senai.fulleducationsys.controller.dto.response.CursoResponse;
import com.senai.fulleducationsys.controller.dto.response.MateriaResponse;

import com.senai.fulleducationsys.service.MateriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("materias")
public class MateriaController {

    private final MateriaService materiaService;

    @GetMapping("/cursos/{id_curso}/materias")
    public ResponseEntity<List<MateriaResponse>> getMateriasPorCurso(
            @PathVariable(name = "id_curso") Long idCurso) {

        List<MateriaResponse> materias = materiaService.getMateriasPorCurso(idCurso);

        return ResponseEntity.ok(materias);
    }

    @PostMapping("/cursos/{id_curso}")
    public ResponseEntity<String> create(
            @Validated @RequestBody CursoRequest materiaRequest,
            @RequestHeader(name = "Authorization") String token,  @PathVariable("id_curso") Long cursoId) {
        materiaService.create(materiaRequest, token.substring(7), cursoId);
        return ResponseEntity.ok("Materia criada com sucesso!");
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<MateriaResponse> getEntityId(
            @PathVariable Long id,
            @RequestHeader (name = "Authorization") String token) {
        MateriaResponse materiaId = materiaService.getEntityIdDto(id, token.substring(7));

        return ResponseEntity.ok(materiaId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MateriaResponse> update(
            @PathVariable Long id,
            @Validated @RequestBody MateriaRequest materiaRequest,
            @RequestHeader (name = "Authorization") String token) {

        MateriaResponse materiaUpdate = materiaService.update(id, materiaRequest, token.substring(7));

        return ResponseEntity.ok(materiaUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<MateriaResponse>> delete(
            @PathVariable Long id,
            @RequestHeader (name = "Authorization") String token) {

        List<MateriaResponse> materiaDelete = materiaService.delete(id, token.substring(7));

        return ResponseEntity.ok(materiaDelete);
    }


}
