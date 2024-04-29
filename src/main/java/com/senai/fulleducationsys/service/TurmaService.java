package com.senai.fulleducationsys.service;

import com.senai.fulleducationsys.controller.dto.request.DocenteRequest;
import com.senai.fulleducationsys.controller.dto.request.TurmaRequest;
import com.senai.fulleducationsys.controller.dto.response.TurmaResponse;
import com.senai.fulleducationsys.datasource.entity.CursoEntity;
import com.senai.fulleducationsys.datasource.entity.DocenteEntity;
import com.senai.fulleducationsys.datasource.entity.TurmaEntity;

import com.senai.fulleducationsys.datasource.repository.CursoRepository;
import com.senai.fulleducationsys.datasource.repository.DocenteRepository;
import com.senai.fulleducationsys.datasource.repository.TurmaRepository;

import com.senai.fulleducationsys.infra.exception.CustomException.CadastroDuplicadoException;
import com.senai.fulleducationsys.infra.exception.CustomException.CampoObrigatorioException;
import com.senai.fulleducationsys.infra.exception.CustomException.NotFoundException;
import com.senai.fulleducationsys.infra.exception.CustomException.UsuarioNaoAutorizadoException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TurmaService {
    private final TurmaRepository turmaRepository;
    private final TokenService tokenService;
    private final DocenteRepository docenteRepository;
    private final CursoRepository cursoRepository;

    public TurmaResponse create(TurmaRequest turmaRequest, String token) {

        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        if (turmaRequest.nome() == null
                || turmaRequest.professorId() == null
                || turmaRequest.cursoId() == null) {
            ArrayList<String> erros = new ArrayList<>();

            if(turmaRequest.nome() == null){
                erros.add("O campo 'alunoId' é obrigatorio");
            }
            if(turmaRequest.professorId() == null){
                erros.add("O campo 'professorId' é obrigatorio");
            }
            if(turmaRequest.cursoId() == null) {
                erros.add("O campo 'cursoId' é obrigatorio");
            }

            throw new CampoObrigatorioException(erros.toString());
        }

        if (turmaRepository.existsByNomeTurma(turmaRequest.nome())) {
            throw new CadastroDuplicadoException("Já existe uma turma com o mesmo nome");
        }

        DocenteEntity professor = docenteRepository.findById(turmaRequest.professorId())
                .orElseThrow(() -> {
                    log.error("Erro, usuario não encontrado");
                    return new NotFoundException("Professor não encontrado com o ID: " + turmaRequest.professorId());
                });

        CursoEntity curso = cursoRepository.findById(turmaRequest.cursoId())
                .orElseThrow(() -> {
                    log.error("Erro, curso não encontrado");
                    return new NotFoundException("Curso não encontrado com o ID: " + turmaRequest.cursoId());
                });

        TurmaEntity turmaEntity = new TurmaEntity();
        turmaEntity.setProfessor(professor);
        turmaEntity.setCurso(curso);
        turmaEntity.setNome(turmaRequest.nome());
        TurmaEntity turmaRegistrado = turmaRepository.save((turmaEntity));

        log.info("Criando turmas-> Salvo com sucesso");
        return new TurmaResponse(turmaRegistrado.getTurmaId(),
                turmaRegistrado.getNome(), turmaRegistrado.getProfessor().getNome(), turmaRegistrado.getCurso().getNomeCurso());
    }

    public  TurmaResponse getEntityIdDto(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        TurmaEntity turmaId = turmaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Erro, turma não encontrada");
                    return new NotFoundException("Turma não encontrada com o ID: " + id);
                });

        log.info("Buscando turma por id ({}) -> Encontrado", id);

        return new TurmaResponse(turmaId.getTurmaId(), turmaId.getNome(), turmaId.getProfessor().getNome(), turmaId.getCurso().getNomeCurso());
    }

    public  TurmaResponse update(Long id, TurmaRequest turmaRequest, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        if (turmaRequest.nome() == null
                || turmaRequest.professorId() == null
                || turmaRequest.cursoId() == null) {
            ArrayList<String> erros = new ArrayList<>();

            if(turmaRequest.nome() == null){
                erros.add("O campo 'alunoId' é obrigatorio");
            }
            if(turmaRequest.professorId() == null){
                erros.add("O campo 'professorId' é obrigatorio");
            }
            if(turmaRequest.cursoId() == null) {
                erros.add("O campo 'cursoId' é obrigatorio");
            }

            throw new CampoObrigatorioException(erros.toString());
        }

        TurmaEntity turmaAtualizado = turmaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Erro, turma não encontrada");
                    return new NotFoundException("Turma não encontrada com o ID: " + id);
                });

        turmaAtualizado.setNome(turmaRequest.nome());

        turmaRepository.save(turmaAtualizado);

        log.info("Alterando turma -> Salvo com sucesso");
        return new TurmaResponse(turmaAtualizado.getTurmaId(), turmaAtualizado.getNome(), turmaAtualizado.getProfessor().getNome(), turmaAtualizado.getCurso().getNomeCurso());
    }

    public List<TurmaResponse> delete(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }
        TurmaEntity turmaDeletado = turmaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Erro, turma não encontrada");
                    return new NotFoundException("Turma não encontrada com o ID: " + id);
                });

        turmaRepository.delete(turmaDeletado);
        log.info("Excluindo turma com id ({}) -> Excluído com sucesso", id);

        List<TurmaEntity> turmasAtualizados = turmaRepository.findAll();

        List<TurmaResponse> turmaResponse = turmasAtualizados.stream()
                .map(turma -> new TurmaResponse(turma.getTurmaId(), turma.getNome(), turma.getProfessor().getNome(), turma.getCurso().getNomeCurso())).collect(Collectors.toList());

        return turmaResponse;

    }

    public List<TurmaResponse> getEntityDto(String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        List<TurmaEntity> turmas = turmaRepository.findAll();

        if(turmas.isEmpty()){
            log.error("Erro, s não existem");
            throw new NotFoundException("Não há turmas cadastrados.");
        }
        log.info("Buscando todos os docentes -> {} Encontrados", turmas.size());
        return turmas.stream().map(
                turma -> new TurmaResponse(turma.getTurmaId(), turma.getNome(), turma.getProfessor().getNome(), turma.getCurso().getNomeCurso())).collect(Collectors.toList());
    }
}
