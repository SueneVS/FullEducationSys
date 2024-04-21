package com.senai.fulleducationsys.service;

import com.senai.fulleducationsys.controller.dto.request.AlunoRequest;
import com.senai.fulleducationsys.controller.dto.response.AlunoResponse;
import com.senai.fulleducationsys.datasource.entity.AlunoEntity;
import com.senai.fulleducationsys.datasource.entity.TurmaEntity;
import com.senai.fulleducationsys.datasource.entity.UsuarioEntity;
import com.senai.fulleducationsys.datasource.repository.AlunoRepository;
import com.senai.fulleducationsys.datasource.repository.TurmaRepository;
import com.senai.fulleducationsys.datasource.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class AlunoService {
    private final AlunoRepository alunoRepository;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final TurmaRepository turmaRepository;

    public AlunoResponse create(AlunoRequest alunoRequest, String token) {

        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new RuntimeException("Acesso não autorizado.");
        }

        UsuarioEntity usuario = usuarioRepository.findById(alunoRequest.usuarioId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com o ID:" + alunoRequest.usuarioId()));
        TurmaEntity turma = turmaRepository.findById(alunoRequest.turmaId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com o ID:" + alunoRequest.turmaId()));

        AlunoEntity alunoEntity = new AlunoEntity();
        alunoEntity.setUsuario(usuario);
        alunoEntity.setNome(alunoRequest.nome());
        alunoEntity.setDataNascimento(alunoRequest.dataNascimento());
        alunoEntity.setTurma(turma);
        AlunoEntity alunoRegistrado = alunoRepository.save((alunoEntity));

        return new AlunoResponse(alunoRegistrado.getAlunoId(),
                alunoRegistrado.getNome(), alunoRegistrado.getDataNascimento(), alunoRegistrado.getUsuario().getUsuarioId(), alunoRegistrado.getTurma().getNome());
    }

    public  AlunoResponse getEntityIdDto(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO") && !papel.equals("RECRUITER")){
            throw new RuntimeException("Acesso não autorizado.");
        }

        AlunoEntity alunoId = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com o ID: " + id));
        return new AlunoResponse(alunoId.getAlunoId(), alunoId.getNome(), alunoId.getDataNascimento(), alunoId.getUsuario().getUsuarioId(), alunoId.getTurma().getNome());
    }

    public  AlunoResponse update(Long id, AlunoRequest alunoRequest, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO") && !papel.equals("RECRUITER")){
            throw new RuntimeException("Acesso não autorizado.");
        }

        AlunoEntity alunoAtualizado = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com o ID: " + id));

        alunoAtualizado.setNome(alunoRequest.nome());

        alunoRepository.save(alunoAtualizado);

        return new AlunoResponse(alunoAtualizado.getAlunoId(), alunoAtualizado.getNome(), alunoAtualizado.getDataNascimento(), alunoAtualizado.getUsuario().getUsuarioId(),alunoAtualizado.getTurma().getNome());
    }

    public List<AlunoResponse> delete(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new RuntimeException("Acesso não autorizado.");
        }
        AlunoEntity alunoDeletado = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com o ID: " + id));

        alunoRepository.delete(alunoDeletado);

        List<AlunoEntity> alunosAtualizados = alunoRepository.findAll();

        List<AlunoResponse> alunoResponses = alunosAtualizados.stream()
                .map(aluno -> new AlunoResponse(aluno.getAlunoId(), aluno.getNome(), aluno.getDataNascimento(), aluno.getUsuario().getUsuarioId(), aluno.getTurma().getNome())).toList();

        return alunoResponses;

    }

    public List<AlunoResponse> getEntityDto(String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO") && !papel.equals("RECRUITER")){
            throw new RuntimeException("Acesso não autorizado.");
        }

        List<AlunoEntity> alunos = alunoRepository.findAll();

        if(alunos.isEmpty()){
            throw new RuntimeException("Não há alunos cadastrados.");
        }

        return alunos.stream().map(
                aluno -> new AlunoResponse(aluno.getAlunoId(), aluno.getNome(), aluno.getDataNascimento(), aluno.getUsuario().getUsuarioId(), aluno.getTurma().getNome())).toList();
    }
}
