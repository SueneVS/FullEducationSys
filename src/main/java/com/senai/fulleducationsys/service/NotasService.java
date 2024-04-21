package com.senai.fulleducationsys.service;

import com.senai.fulleducationsys.controller.dto.request.NotasRequest;
import com.senai.fulleducationsys.controller.dto.response.NotasResponse;
import com.senai.fulleducationsys.datasource.entity.*;
import com.senai.fulleducationsys.datasource.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotasService {
    private final NotasRepository notasRepository;
    private final TokenService tokenService;
    private final DocenteRepository docenteRepository;
    private final AlunoRepository alunoRepository;
    private final MateriaRepository materiaRepository;

    public NotasResponse create(NotasRequest notasRequest, String token) {

        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new RuntimeException("Acesso não autorizado.");
        }

        AlunoEntity aluno = alunoRepository.findById(notasRequest.alunoId())
                .orElseThrow(() -> new RuntimeException("Notas não encontrado com o ID:" + notasRequest.professorId()));

        DocenteEntity professor = docenteRepository.findById(notasRequest.professorId())
                .orElseThrow(() -> new RuntimeException("Notas não encontrado com o ID:" + notasRequest.professorId()));

        MateriaEntity materia = materiaRepository.findById(notasRequest.materiaId())
                .orElseThrow(() -> new RuntimeException("Notas não encontrado com o ID:" + notasRequest.materiaId()));

        NotasEntity notasEntity = new NotasEntity();
        notasEntity.setAluno(aluno);
        notasEntity.setProfessor(professor);
        notasEntity.setMateria(materia);
        notasEntity.setNota(notasRequest.nota());
        notasEntity.setDataNota(notasRequest.dataNota());
        NotasEntity notasRegistrada = notasRepository.save((notasEntity));

        return new NotasResponse(notasRegistrada.getNotasId(), notasRegistrada.getAluno().getAlunoId(), notasRegistrada.getProfessor().getDocenteId(),
                notasRegistrada.getMateria().getMateriaId(), notasRegistrada.getNota(), notasRegistrada.getDataNota());
    }

    public  NotasResponse getEntityId(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO") && !papel.equals("RECRUITER")){
            throw new RuntimeException("Acesso não autorizado.");
        }

        NotasEntity notasId = notasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notas não encontrado com o ID: " + id));
        return new NotasResponse(notasId.getNotasId(), notasId.getAluno().getAlunoId(), notasId.getProfessor().getDocenteId(),
                notasId.getMateria().getMateriaId(), notasId.getNota(), notasId.getDataNota());
    }

    public  NotasResponse update(Long id, NotasRequest notasRequest, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO") && !papel.equals("RECRUITER")){
            throw new RuntimeException("Acesso não autorizado.");
        }

        NotasEntity notasAtualizada = notasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notas não encontrado com o ID: " + id));

        notasAtualizada.setNota(notasRequest.nota());

        notasRepository.save(notasAtualizada);

        return new NotasResponse(notasAtualizada.getNotasId(), notasAtualizada.getAluno().getAlunoId(), notasAtualizada.getProfessor().getDocenteId(),
                notasAtualizada.getMateria().getMateriaId(), notasAtualizada.getNota(), notasAtualizada.getDataNota());
    }

    public List<NotasResponse> delete(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new RuntimeException("Acesso não autorizado.");
        }
        NotasEntity notasDeletado = notasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notas não encontrado com o ID: " + id));

        notasRepository.delete(notasDeletado);

        List<NotasEntity> notassAtualizados = notasRepository.findAll();

        List<NotasResponse> notasResponses = notassAtualizados.stream()
                .map(notas -> new NotasResponse(notas.getNotasId(), notas.getAluno().getAlunoId(), notas.getProfessor().getDocenteId(), notas.getMateria().getMateriaId(), notas.getNota(),notas.getDataNota())).toList();

        return notasResponses;

    }

    public List<NotasResponse> getNotasPorAluno(Long notasId, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO") && !papel.equals("RECRUITER")){
            throw new RuntimeException("Acesso não autorizado.");
        }

        List<NotasEntity> notas = notasRepository.findAllByAlunoId(notasId);

        if(notas.isEmpty()){
            throw new RuntimeException("Não há notass cadastrados.");
        }

        return notas.stream().map(
                nota -> new NotasResponse(
                        nota.getNotasId(),
                        nota.getAluno().getAlunoId(),
                        nota.getProfessor().getDocenteId(),
                        nota.getMateria().getMateriaId(),
                        nota.getNota(),nota.getDataNota()
                )).toList();
    }
}
