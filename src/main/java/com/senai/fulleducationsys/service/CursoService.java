package com.senai.fulleducationsys.service;

import com.senai.fulleducationsys.controller.dto.request.CursoRequest;
import com.senai.fulleducationsys.controller.dto.response.CursoResponse;
import com.senai.fulleducationsys.datasource.entity.CursoEntity;

import com.senai.fulleducationsys.datasource.repository.CursoRepository;

import com.senai.fulleducationsys.infra.exception.CustomException.CadastroDuplicadoException;
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
public class CursoService {

    private final CursoRepository cursoRepository;
    private final TokenService tokenService;

    public CursoResponse create(CursoRequest cursoRequest, String token) {

        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        if (cursoRequest.nome() == null){
            throw new CampoObrigatorioException("O campo 'nome' é obrigatorio");
        }

        if (cursoRepository.existsByNomeCurso(cursoRequest.nome())) {
            throw new CadastroDuplicadoException("Já existe um curso com o mesmo nome");
        }

        CursoEntity cursoEntity = new CursoEntity();
        cursoEntity.setNomeCurso(cursoRequest.nome());
        CursoEntity cursoRegistrado = cursoRepository.save((cursoEntity));

        log.info("Criando curso-> Salvo com sucesso");

        return new CursoResponse(cursoRegistrado.getCursoId(),
                cursoRegistrado.getNomeCurso());
    }
    public  CursoResponse getEntityIdDto(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }
        CursoEntity cursoId = cursoRepository.findById(id)
                .orElseThrow(
                        () ->{
                            log.error("Erro, o curso não existe");
                            return new NotFoundException("Curso não encontrado com o ID:" + id);
                        }
                );

        log.info("Buscando curso por id ({}) -> Encontrado", id);

        return new CursoResponse(cursoId.getCursoId(), cursoId.getNomeCurso());
    }

    public  CursoResponse update(Long id, CursoRequest cursoRequest, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        if (cursoRequest.nome() == null){
            throw new CampoObrigatorioException("O campo 'nome' é obrigatorio");
        }

        if (cursoRepository.existsByNomeCurso(cursoRequest.nome())) {
            throw new CadastroDuplicadoException("Já existe um curso com o mesmo nome");
        }

        CursoEntity cursoAtualizado = cursoRepository.findById(id)
                .orElseThrow(() -> {
                            log.error("Erro, o curso não existe");
                            return new NotFoundException("Curso não encontrado com o ID:" + id);
                        }
                );

        cursoAtualizado.setNomeCurso(cursoRequest.nome());

        cursoRepository.save(cursoAtualizado);
        log.info("Alterando curso -> Salvo com sucesso");

        return new CursoResponse(cursoAtualizado.getCursoId(), cursoAtualizado.getNomeCurso());
    }

    public List<CursoResponse> delete(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }
        CursoEntity cursoDeletado = cursoRepository.findById(id)
                .orElseThrow(() -> {
                            log.error("Erro, o curso não existe");
                            return new NotFoundException("Curso não encontrado com o ID:" + id);
                        }
                );

        cursoRepository.delete(cursoDeletado);
        log.info("Excluindo curso com id ({}) -> Excluído com sucesso", id);

        List<CursoEntity> cursosAtualizados = cursoRepository.findAll();

        List<CursoResponse> cursoResponses = cursosAtualizados.stream()
                .map(curso -> new CursoResponse(curso.getCursoId(), curso.getNomeCurso())).toList();

        return cursoResponses;

    }

    public List<CursoResponse> getEntityDto(String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        List<CursoEntity> cursos = cursoRepository.findAll();

        if(cursos.isEmpty()){
            log.error("Erro, cursos não existem");
            throw new RuntimeException("Não há cursos cadastrados.");
        }
        log.info("Buscando todos os cursos -> {} Encontrados", cursos.size());

        return cursos.stream().map(
                curso -> new CursoResponse(curso.getCursoId(), curso.getNomeCurso())).toList();
    }
}
