package com.senai.fulleducationsys.service;

import com.senai.fulleducationsys.controller.dto.request.CursoRequest;
import com.senai.fulleducationsys.controller.dto.response.CursoResponse;
import com.senai.fulleducationsys.datasource.entity.CursoEntity;

import com.senai.fulleducationsys.datasource.repository.CursoRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class CursoService {

    private final CursoRepository cursoRepository;
    private final TokenService tokenService;

    public CursoResponse create(CursoRequest cursoRequest, String token) {

        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new RuntimeException("Acesso não autorizado.");
        }

        CursoEntity cursoEntity = new CursoEntity();
        cursoEntity.setNomeCurso(cursoRequest.nome());
        CursoEntity cursoRegistrado = cursoRepository.save((cursoEntity));

        return new CursoResponse(cursoRegistrado.getCursoId(),
                cursoRegistrado.getNomeCurso());
    }
    public  CursoResponse getEntityIdDto(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO")){
            throw new RuntimeException("Acesso não autorizado.");
        }
        CursoEntity cursoId = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com o ID: " + id));
        return new CursoResponse(cursoId.getCursoId(), cursoId.getNomeCurso());
    }

    public  CursoResponse update(Long id, CursoRequest cursoRequest, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO")){
            throw new RuntimeException("Acesso não autorizado.");
        }

        CursoEntity cursoAtualizado = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com o ID: " + id));

        cursoAtualizado.setNomeCurso(cursoRequest.nome());

        cursoRepository.save(cursoAtualizado);

        return new CursoResponse(cursoAtualizado.getCursoId(), cursoAtualizado.getNomeCurso());
    }

    public List<CursoResponse> delete(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new RuntimeException("Acesso não autorizado.");
        }
        CursoEntity cursoDeletado = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com o ID: " + id));

        cursoRepository.delete(cursoDeletado);

        List<CursoEntity> cursosAtualizados = cursoRepository.findAll();

        List<CursoResponse> cursoResponses = cursosAtualizados.stream()
                .map(curso -> new CursoResponse(curso.getCursoId(), curso.getNomeCurso())).toList();

        return cursoResponses;

    }

    public List<CursoResponse> getEntityDto(String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO")){
            throw new RuntimeException("Acesso não autorizado.");
        }

        List<CursoEntity> cursos = cursoRepository.findAll();

        if(cursos.isEmpty()){
            throw new RuntimeException("Não há cursos cadastrados.");
        }

        return cursos.stream().map(
                curso -> new CursoResponse(curso.getCursoId(), curso.getNomeCurso())).toList();
    }
}
