package com.senai.fulleducationsys.service;

import com.senai.fulleducationsys.controller.dto.request.TurmaRequest;
import com.senai.fulleducationsys.controller.dto.response.TurmaResponse;
import com.senai.fulleducationsys.datasource.entity.CursoEntity;
import com.senai.fulleducationsys.datasource.entity.DocenteEntity;
import com.senai.fulleducationsys.datasource.entity.TurmaEntity;

import com.senai.fulleducationsys.datasource.repository.CursoRepository;
import com.senai.fulleducationsys.datasource.repository.DocenteRepository;
import com.senai.fulleducationsys.datasource.repository.TurmaRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TurmaService {
    private final TurmaRepository turmaRepository;
    private final TokenService tokenService;
    private final DocenteRepository docenteRepository;
    private final CursoRepository cursoRepository;

    public TurmaResponse create(TurmaRequest turmaRequest, String token) {

        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new RuntimeException("Acesso não autorizado.");
        }

        DocenteEntity professor = docenteRepository.findById(turmaRequest.professorId())
                .orElseThrow(() -> new RuntimeException("Turma não encontrado com o ID:" + turmaRequest.professorId()));

        CursoEntity curso = cursoRepository.findById(turmaRequest.cursoId())
                .orElseThrow(() -> new RuntimeException("Turma não encontrado com o ID:" + turmaRequest.professorId()));

        TurmaEntity turmaEntity = new TurmaEntity();
        turmaEntity.setProfessor(professor);
        turmaEntity.setCurso(curso);
        turmaEntity.setNome(turmaRequest.nome());
        TurmaEntity turmaRegistrado = turmaRepository.save((turmaEntity));

        return new TurmaResponse(turmaRegistrado.getTurmaId(),
                turmaRegistrado.getNome(), turmaRegistrado.getProfessor().getDocenteId(), turmaRegistrado.getCurso().getNomeCurso());
    }

    public  TurmaResponse getEntityIdDto(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO")){
            throw new RuntimeException("Acesso não autorizado.");
        }

        TurmaEntity turmaId = turmaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turma não encontrado com o ID: " + id));
        return new TurmaResponse(turmaId.getTurmaId(), turmaId.getNome(), turmaId.getProfessor().getDocenteId(), turmaId.getCurso().getNomeCurso());
    }

    public  TurmaResponse update(Long id, TurmaRequest turmaRequest, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO")){
            throw new RuntimeException("Acesso não autorizado.");
        }

        TurmaEntity turmaAtualizado = turmaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turma não encontrado com o ID: " + id));

        turmaAtualizado.setNome(turmaRequest.nome());

        turmaRepository.save(turmaAtualizado);

        return new TurmaResponse(turmaAtualizado.getTurmaId(), turmaAtualizado.getNome(), turmaAtualizado.getProfessor().getDocenteId(), turmaAtualizado.getCurso().getNomeCurso());
    }

    public List<TurmaResponse> delete(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new RuntimeException("Acesso não autorizado.");
        }
        TurmaEntity turmaDeletado = turmaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turma não encontrado com o ID: " + id));

        turmaRepository.delete(turmaDeletado);

        List<TurmaEntity> turmasAtualizados = turmaRepository.findAll();

        List<TurmaResponse> turmaResponse = turmasAtualizados.stream()
                .map(turma -> new TurmaResponse(turma.getTurmaId(), turma.getNome(), turma.getProfessor().getDocenteId(), turma.getCurso().getNomeCurso())).collect(Collectors.toList());

        return turmaResponse;

    }

    public List<TurmaResponse> getEntityDto(String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO")){
            throw new RuntimeException("Acesso não autorizado.");
        }

        List<TurmaEntity> turmas = turmaRepository.findAll();

        if(turmas.isEmpty()){
            throw new RuntimeException("Não há turmas cadastrados.");
        }

        return turmas.stream().map(
                turma -> new TurmaResponse(turma.getTurmaId(), turma.getNome(), turma.getProfessor().getDocenteId(), turma.getCurso().getNomeCurso())).collect(Collectors.toList());
    }
}
