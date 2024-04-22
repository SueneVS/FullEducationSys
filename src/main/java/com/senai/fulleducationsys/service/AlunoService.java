package com.senai.fulleducationsys.service;

import com.senai.fulleducationsys.controller.dto.request.AlunoRequest;
import com.senai.fulleducationsys.controller.dto.response.AlunoResponse;
import com.senai.fulleducationsys.datasource.entity.AlunoEntity;
import com.senai.fulleducationsys.datasource.entity.TurmaEntity;
import com.senai.fulleducationsys.datasource.entity.UsuarioEntity;
import com.senai.fulleducationsys.datasource.repository.AlunoRepository;
import com.senai.fulleducationsys.datasource.repository.TurmaRepository;
import com.senai.fulleducationsys.datasource.repository.UsuarioRepository;

import com.senai.fulleducationsys.infra.exception.CustomException.CampoObrigatorioException;
import com.senai.fulleducationsys.infra.exception.CustomException.NotFoundException;
import com.senai.fulleducationsys.infra.exception.CustomException.UsuarioNaoAutorizadoException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
@Service
@AllArgsConstructor
@Slf4j
public class AlunoService {
    private final AlunoRepository alunoRepository;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final TurmaRepository turmaRepository;


    public AlunoResponse create(AlunoRequest alunoRequest, String token) {

        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        if (alunoRequest.nome() == null
                || alunoRequest.dataNascimento() == null
                || alunoRequest.usuarioId() == null
                || alunoRequest.turmaId() == null) {
            ArrayList<String> erros = new ArrayList<>();

            if(alunoRequest.nome() == null){
                erros.add("O campo 'nome' é obrigatorio");
            }
            if(alunoRequest.dataNascimento() == null){
                erros.add("O campo 'dataNascimento' é obrigatorio");
            }
            if(alunoRequest.usuarioId() == null) {
                erros.add("O campo 'usuarioId' é obrigatorio");
            }
            if(alunoRequest.turmaId() == null){
                erros.add("O campo 'turmaId' é obrigatorio");
            }

            throw new CampoObrigatorioException(erros.toString());
        }

        UsuarioEntity usuario = usuarioRepository.findById(alunoRequest.usuarioId())
                .orElseThrow(
                        () ->{
                            log.error("Erro, usuário não existe");
                            return new NotFoundException("Aluno não encontrado com o ID:" + alunoRequest.usuarioId());
                        }
                );

        TurmaEntity turma = turmaRepository.findById(alunoRequest.turmaId())
                .orElseThrow(
                        () ->{
                            log.error("Erro, turma não existe");
                           return new NotFoundException("Turma não encontrada com o ID:" + alunoRequest.turmaId());
                        }
                );

        AlunoEntity alunoEntity = new AlunoEntity();
        alunoEntity.setUsuario(usuario);
        alunoEntity.setNome(alunoRequest.nome());
        alunoEntity.setDataNascimento(alunoRequest.dataNascimento());
        alunoEntity.setTurma(turma);
        AlunoEntity alunoRegistrado = alunoRepository.save((alunoEntity));

        log.info("Criando aluno-> Salvo com sucesso");

        return new AlunoResponse(alunoRegistrado.getAlunoId(),
                alunoRegistrado.getNome(), alunoRegistrado.getDataNascimento(), alunoRegistrado.getUsuario().getUsuarioId(), alunoRegistrado.getTurma().getNome());
    }

    public  AlunoResponse getEntityIdDto(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO") && !papel.equals("RECRUITER")){
            log.error("Erro, Acesso não autorizado.");
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        AlunoEntity alunoId = alunoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Erro, usuário não existe");
                    return new NotFoundException("Aluno não encontrado com o ID: " + id);
                }
                );

        log.info("Buscando aluno por id ({}) -> Encontrado", id);

        return new AlunoResponse(alunoId.getAlunoId(), alunoId.getNome(), alunoId.getDataNascimento(), alunoId.getUsuario().getUsuarioId(), alunoId.getTurma().getNome());
    }

    public  AlunoResponse update(Long id, AlunoRequest alunoRequest, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO") && !papel.equals("RECRUITER")){
            log.error("Erro, Acesso não autorizado.");
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }
        if (alunoRequest.nome() == null
                || alunoRequest.dataNascimento() == null
                || alunoRequest.usuarioId() == null
                || alunoRequest.turmaId() == null) {
            ArrayList<String> erros = new ArrayList<>();

            if(alunoRequest.nome() == null){
                erros.add("O campo 'nome' é obrigatorio");
            }
            if(alunoRequest.dataNascimento() == null){
                erros.add("O campo 'dataNascimento' é obrigatorio");
            }
            if(alunoRequest.usuarioId() == null){
                erros.add("O campo 'usuarioId' é obrigatorio");
            }
            if(alunoRequest.turmaId() == null){
                erros.add("O campo 'turmaId' é obrigatorio");
            }

            throw new CampoObrigatorioException(erros.toString());
        }

        AlunoEntity alunoAtualizado = alunoRepository.findById(id)
                .orElseThrow(() ->{
                    log.error("Erro, usuário não existe");
                    return new NotFoundException("Aluno não encontrado com o ID: " + id);}
                );

        alunoAtualizado.setNome(alunoRequest.nome());
        alunoAtualizado.setDataNascimento(alunoRequest.dataNascimento());

        if (!alunoAtualizado.getTurma().getTurmaId().equals(alunoRequest.turmaId())) {
            TurmaEntity turma = turmaRepository.findById(alunoRequest.turmaId())
                    .orElseThrow(() -> {
                        log.error("Erro, turma não encontrada");
                        return new NotFoundException("Turma não encontrada com o ID: " + alunoRequest.turmaId());
                    });
            alunoAtualizado.setTurma(turma);
        }

        alunoRepository.save(alunoAtualizado);
        log.info("Alterando aluno -> Salvo com sucesso");

        return new AlunoResponse(alunoAtualizado.getAlunoId(), alunoAtualizado.getNome(), alunoAtualizado.getDataNascimento(), alunoAtualizado.getUsuario().getUsuarioId(),alunoAtualizado.getTurma().getNome());
    }

    public List<AlunoResponse> delete(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            log.error("Erro, Acesso não autorizado.");
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }
        AlunoEntity alunoDeletado = alunoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Erro, usuário não existe");
                    return new NotFoundException("Aluno não encontrado com o ID: " + id);}
                );

        alunoRepository.delete(alunoDeletado);
        log.info("Excluindo aluno com id ({}) -> Excluído com sucesso", id);

        List<AlunoEntity> alunosAtualizados = alunoRepository.findAll();

        List<AlunoResponse> alunoResponses = alunosAtualizados.stream()
                .map(aluno -> new AlunoResponse(aluno.getAlunoId(), aluno.getNome(), aluno.getDataNascimento(), aluno.getUsuario().getUsuarioId(), aluno.getTurma().getNome())).toList();
        return alunoResponses;

    }

    public List<AlunoResponse> getEntityDto(String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO") && !papel.equals("RECRUITER")){
            log.error("Erro, Acesso não autorizado.");
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        List<AlunoEntity> alunos = alunoRepository.findAll();

        if(alunos.isEmpty()){
            log.error("Erro, usuários não existem");
            throw new NotFoundException("Não há alunos cadastrados.");
        }

        log.info("Buscando todos os alunos -> {} Encontrados", alunos.size());

        return alunos.stream().map(
                aluno -> new AlunoResponse(aluno.getAlunoId(), aluno.getNome(), aluno.getDataNascimento(), aluno.getUsuario().getUsuarioId(), aluno.getTurma().getNome())).toList();
    }
}
