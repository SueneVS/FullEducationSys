package com.senai.fulleducationsys.service;

import com.senai.fulleducationsys.controller.dto.request.NotasRequest;
import com.senai.fulleducationsys.controller.dto.response.NotasResponse;
import com.senai.fulleducationsys.datasource.entity.*;
import com.senai.fulleducationsys.datasource.repository.*;
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
public class NotasService {
    private final NotasRepository notasRepository;
    private final TokenService tokenService;
    private final DocenteRepository docenteRepository;
    private final AlunoRepository alunoRepository;
    private final MateriaRepository materiaRepository;

    public NotasResponse create(NotasRequest notasRequest, String token) {

        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        if (notasRequest.alunoId() == null
                || notasRequest.professorId() == null
                || notasRequest.materiaId() == null
                || notasRequest.nota() == null
                || notasRequest.dataNota() == null) {
            ArrayList<String> erros = new ArrayList<>();

            if(notasRequest.alunoId() == null){
                erros.add("O campo 'alunoId' é obrigatorio");
            }
            if(notasRequest.professorId() == null){
                erros.add("O campo 'professorId' é obrigatorio");
            }
            if(notasRequest.materiaId() == null) {
                erros.add("O campo 'materiaId' é obrigatorio");
            }
            if(notasRequest.nota() == null){
                erros.add("O campo 'nota' é obrigatorio");
            }
            if(notasRequest.dataNota() == null) {
                erros.add("O campo 'dataNota' é obrigatorio");
            }

            throw new CampoObrigatorioException(erros.toString());
        }

        AlunoEntity aluno = alunoRepository.findById(notasRequest.alunoId())
                .orElseThrow(() -> {
                    log.error("Erro, usuario não encontrado");
                    return new NotFoundException("Aluno não encontrado com o ID: " + notasRequest.alunoId());
                });

        DocenteEntity professor = docenteRepository.findById(notasRequest.professorId())
                .orElseThrow(() -> {
                    log.error("Erro, usuario não encontrada");
                    return new NotFoundException("Professor não encontrado com o ID: " + notasRequest.professorId());
                });

        MateriaEntity materia = materiaRepository.findById(notasRequest.materiaId())
                .orElseThrow(() -> {
                    log.error("Erro, materia não encontrada");
                    return new NotFoundException("Materia não encontrada com o ID: " + notasRequest.materiaId());
                });

        NotasEntity notasEntity = new NotasEntity();
        notasEntity.setAluno(aluno);
        notasEntity.setProfessor(professor);
        notasEntity.setMateria(materia);
        notasEntity.setNota(notasRequest.nota());
        notasEntity.setDataNota(notasRequest.dataNota());
        NotasEntity notasRegistrada = notasRepository.save((notasEntity));

        log.info("Criando notas-> Salvo com sucesso");

        return new NotasResponse(notasRegistrada.getNotasId(), notasRegistrada.getAluno().getAlunoId(), notasRegistrada.getProfessor().getDocenteId(),
                notasRegistrada.getMateria().getMateriaId(), notasRegistrada.getNota(), notasRegistrada.getDataNota());
    }

    public  NotasResponse getEntityId(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO") && !papel.equals("RECRUITER")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        NotasEntity notasId = notasRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Erro, materia não encontrada");
                    return new NotFoundException("Docente não encontrada com o ID: " + id);
                });

        log.info("Buscando notas por id ({}) -> Encontrado", id);

        return new NotasResponse(notasId.getNotasId(), notasId.getAluno().getAlunoId(), notasId.getProfessor().getDocenteId(),
                notasId.getMateria().getMateriaId(), notasId.getNota(), notasId.getDataNota());
    }

    public  NotasResponse update(Long id, NotasRequest notasRequest, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO") && !papel.equals("RECRUITER")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        if (notasRequest.alunoId() == null
                || notasRequest.professorId() == null
                || notasRequest.materiaId() == null
                || notasRequest.nota() == null
                || notasRequest.dataNota() == null) {
            ArrayList<String> erros = new ArrayList<>();

            if(notasRequest.alunoId() == null){
                erros.add("O campo 'alunoId' é obrigatorio");
            }
            if(notasRequest.professorId() == null){
                erros.add("O campo 'professorId' é obrigatorio");
            }
            if(notasRequest.materiaId() == null) {
                erros.add("O campo 'materiaId' é obrigatorio");
            }
            if(notasRequest.nota() == null){
                erros.add("O campo 'nota' é obrigatorio");
            }
            if(notasRequest.dataNota() == null) {
                erros.add("O campo 'dataNota' é obrigatorio");
            }

            throw new CampoObrigatorioException(erros.toString());
        }


        NotasEntity notasAtualizada = notasRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Erro, materia não encontrada");
                    return new NotFoundException("Nota não encontrada com o ID: " + id);
                });

        notasAtualizada.setNota(notasRequest.nota());
        notasAtualizada.setDataNota(notasRequest.dataNota());

        notasRepository.save(notasAtualizada);

        log.info("Alterando nota -> Salvo com sucesso");
        return new NotasResponse(notasAtualizada.getNotasId(), notasAtualizada.getAluno().getAlunoId(), notasAtualizada.getProfessor().getDocenteId(),
                notasAtualizada.getMateria().getMateriaId(), notasAtualizada.getNota(), notasAtualizada.getDataNota());
    }

    public List<NotasResponse> delete(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }
        NotasEntity notasDeletado = notasRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Erro, nota não encontrada");
                    return new NotFoundException("Nota não encontrada com o ID: " + id);
                });

        notasRepository.delete(notasDeletado);
        log.info("Excluindo nota com id ({}) -> Excluído com sucesso", id);

        List<NotasEntity> notassAtualizados = notasRepository.findAll();

        List<NotasResponse> notasResponses = notassAtualizados.stream()
                .map(notas -> new NotasResponse(notas.getNotasId(), notas.getAluno().getAlunoId(), notas.getProfessor().getDocenteId(), notas.getMateria().getMateriaId(), notas.getNota(),notas.getDataNota())).toList();

        return notasResponses;

    }

    public List<Double> getNotasPorAluno(Long notasId, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO") && !papel.equals("RECRUITER")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        List<NotasEntity> notas = notasRepository.findAllByAlunoId(notasId);

        if(notas.isEmpty()){
            log.error("Erro, notas não existem");
            throw new NotFoundException("Não há notas cadastrados.");
        }

        log.info("Buscando todos os docentes -> {} Encontrados", notas.size());

        return notas.stream()
                .map(NotasEntity::getNota)
                .collect(Collectors.toList());
    }
}
