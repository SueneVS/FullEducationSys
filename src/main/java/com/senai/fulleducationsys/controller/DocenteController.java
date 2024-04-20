package com.senai.fulleducationsys.controller;

import com.senai.fulleducationsys.controller.dto.request.DocenteRequest;
import com.senai.fulleducationsys.controller.dto.response.DocenteResponse;
import com.senai.fulleducationsys.service.DocenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("docentes")
public class DocenteController {
    private final DocenteService docenteService;

    @PostMapping()
    public ResponseEntity<DocenteResponse> create(
            @Validated @RequestBody DocenteRequest docenteRequest,
            @RequestHeader(name = "Authorization") String token) {
        DocenteResponse response = docenteService.create(docenteRequest, token.substring(7));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocenteResponse> getEntityId(
            @PathVariable Long id,
            @RequestHeader (name = "Authorization") String token) {
        DocenteResponse docenteId = docenteService.getEntityIdDto(id, token.substring(7));

        return ResponseEntity.ok(docenteId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocenteResponse> update(
            @PathVariable Long id,
            @Validated @RequestBody DocenteRequest docenteRequest,
            @RequestHeader (name = "Authorization") String token) {

        DocenteResponse docenteUpdate = docenteService.update(id, docenteRequest, token.substring(7));

        return ResponseEntity.ok(docenteUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<DocenteResponse>> delete(
            @PathVariable Long id,
            @RequestHeader (name = "Authorization") String token) {

        List<DocenteResponse> docenteDelete = docenteService.delete(id, token.substring(7));

        return ResponseEntity.ok(docenteDelete);
    }

    @GetMapping()
    public ResponseEntity<List<DocenteResponse>> getEntity(
            @RequestHeader (name = "Authorization") String token) {

        List<DocenteResponse> docentes = docenteService.getEntityDto(token.substring(7));

        return ResponseEntity.ok(docentes);
    }
}
