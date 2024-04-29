package com.senai.fulleducationsys.service;

import com.senai.fulleducationsys.controller.dto.request.MateriaRequest;

import com.senai.fulleducationsys.controller.dto.response.MateriaResponse;
import com.senai.fulleducationsys.datasource.entity.CursoEntity;
import com.senai.fulleducationsys.datasource.entity.MateriaEntity;
import com.senai.fulleducationsys.datasource.repository.CursoRepository;
import com.senai.fulleducationsys.datasource.repository.MateriaRepository;
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
@Slf4j
@AllArgsConstructor
public class MateriaService {

    private final MateriaRepository materiaRepository;
    private final TokenService tokenService;
    private final CursoRepository cursoRepository;

    public MateriaResponse create(MateriaRequest materiaRequest, String token) {

        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        if (materiaRequest.nome() == null || materiaRequest.cursoId() == null){

            ArrayList<String> erros = new ArrayList<>();

            if(materiaRequest.nome() == null){
                erros.add("O campo 'nome' é obrigatorio");
            }
            if(materiaRequest.cursoId() == null){
                erros.add("O campo 'cursoId' é obrigatorio");
            }

            throw new CampoObrigatorioException(erros.toString());
        }

        if (materiaRepository.existsByNomeMateria(materiaRequest.nome())) {
            throw new CadastroDuplicadoException("Já existe uma materia com o mesmo nome");
        }

       CursoEntity curso = cursoRepository.findById(materiaRequest.cursoId())
               .orElseThrow(
                       () ->{
                           log.error("Erro, curso não existe");
                           return new NotFoundException("Curso não encontrado com o ID:" + materiaRequest.cursoId());
                       }
               );

        MateriaEntity materiaEntity = new MateriaEntity();
        materiaEntity.setCurso(curso);
        materiaEntity.setNome(materiaRequest.nome());
        MateriaEntity materiaRegistrada = materiaRepository.save((materiaEntity));

        log.info("Criando materia-> Salvo com sucesso");
        return new MateriaResponse(materiaRegistrada.getMateriaId(),
                materiaRegistrada.getNome(), materiaRegistrada.getCurso().getNomeCurso());
    }
    public  MateriaResponse getEntityIdDto(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }
        MateriaEntity materiaId = materiaRepository.findById(id)
                .orElseThrow(
                        () ->{
                            log.error("Erro, materia não existe");
                            return new NotFoundException("Materia não encontrado com o ID:" + id);
                        }
               );

        log.info("Buscando materia por id ({}) -> Encontrado", id);

        return new MateriaResponse(materiaId.getMateriaId(), materiaId.getNome(), materiaId.getCurso().getNomeCurso());
    }

    public  MateriaResponse update(Long id, MateriaRequest materiaRequest, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        if (materiaRequest.nome() == null || materiaRequest.cursoId() == null){

            ArrayList<String> erros = new ArrayList<>();

            if(materiaRequest.nome() == null){
                erros.add("O campo 'nome' é obrigatorio");
            }
            if(materiaRequest.cursoId() == null){
                erros.add("O campo 'cursoId' é obrigatorio");
            }

            throw new CampoObrigatorioException(erros.toString());
        }

        MateriaEntity materiaAtualizada = materiaRepository.findById(id)
                .orElseThrow(
                        () ->{
                            log.error("Erro, materia não existe");
                            return new NotFoundException("Materia não encontrada com o ID:" + id);
                        }
                );

        materiaAtualizada.setNome(materiaRequest.nome());

        materiaRepository.save(materiaAtualizada);

        log.info("Alterando materia -> Salvo com sucesso");

        return new MateriaResponse(materiaAtualizada.getMateriaId(), materiaAtualizada.getNome(), materiaAtualizada.getCurso().getNomeCurso());
    }

    public List<MateriaResponse> delete(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }
        MateriaEntity materiaDeletado = materiaRepository.findById(id)
                .orElseThrow(
                        () ->{
                            log.error("Erro, materia não existe");
                            return new NotFoundException("Materia não encontrada com o ID:" + id);
                        }
                );

        materiaRepository.delete(materiaDeletado);
        log.info("Excluindo materia com id ({}) -> Excluído com sucesso", id);

        List<MateriaEntity> materiasAtualizados = materiaRepository.findAll();

        List<MateriaResponse> materiaResponses = materiasAtualizados.stream()
                .map(materia -> new MateriaResponse(materia.getMateriaId(), materia.getNome(), materia.getCurso().getNomeCurso())).toList();

        return materiaResponses;


}

    public List<MateriaResponse> getMateriasPorCurso(Long cursoId, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        List<MateriaEntity> materias = materiaRepository.findAllByCursoId(cursoId);

        if(materias.isEmpty()){
            log.error("Erro, materias não existem");
            throw new NotFoundException("Não há materias cadastradas.");
        }


        log.info("Buscando materia por curso ({}) -> Encontrado", cursoId);
        return materias.stream().map(
                materia ->new MateriaResponse(
                        materia.getMateriaId(),
                        materia.getNome(),
                        materia.getCurso().getNomeCurso()
                )).toList();
    }

}
