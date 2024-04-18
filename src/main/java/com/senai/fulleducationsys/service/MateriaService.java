package com.senai.fulleducationsys.service;

import com.senai.fulleducationsys.controller.dto.request.CursoRequest;
import com.senai.fulleducationsys.controller.dto.request.MateriaRequest;
import com.senai.fulleducationsys.controller.dto.response.CursoResponse;
import com.senai.fulleducationsys.controller.dto.response.MateriaResponse;
import com.senai.fulleducationsys.datasource.entity.CursoEntity;
import com.senai.fulleducationsys.datasource.entity.MateriaEntity;
import com.senai.fulleducationsys.datasource.repository.CursoRepository;
import com.senai.fulleducationsys.datasource.repository.MateriaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class MateriaService {

    private final MateriaRepository materiaRepository;
    private final TokenService tokenService;
    private final CursoRepository cursoRepository;

    public MateriaResponse create(CursoRequest materiaRequest, String token, Long cursoId) {

        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new RuntimeException("Acesso não autorizado.");
        }

       CursoEntity curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        MateriaEntity materiaEntity = new MateriaEntity();
        materiaEntity.setCurso(curso);
        materiaEntity.setNomeMateria(materiaRequest.nome());
        MateriaEntity materiaRegistrada = materiaRepository.save((materiaEntity));

        return new MateriaResponse(materiaRegistrada.getMateriaId(),
                materiaRegistrada.getNomeMateria(), materiaRegistrada.getCurso().getCursoId());
    }
    public  MateriaResponse getEntityIdDto(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO")){
            throw new RuntimeException("Acesso não autorizado.");
        }
        MateriaEntity materiaId = materiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Materia não encontrado com o ID: " + id));
        return new MateriaResponse(materiaId.getMateriaId(), materiaId.getNomeMateria(), materiaId.getCurso().getCursoId());
    }

    public  MateriaResponse update(Long id, MateriaRequest materiaRequest, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO")){
            throw new RuntimeException("Acesso não autorizado.");
        }

        MateriaEntity materiaAtualizada = materiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Materia não encontrado com o ID: " + id));

        materiaAtualizada.setNomeMateria(materiaRequest.nome());

        materiaRepository.save(materiaAtualizada);

        return new MateriaResponse(materiaAtualizada.getMateriaId(), materiaAtualizada.getNomeMateria(), materiaAtualizada.getCurso().getCursoId());
    }

    public List<MateriaResponse> delete(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new RuntimeException("Acesso não autorizado.");
        }
        MateriaEntity materiaDeletado = materiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Materia não encontrado com o ID: " + id));

        materiaRepository.delete(materiaDeletado);

        List<MateriaEntity> materiasAtualizados = materiaRepository.findAll();

        List<MateriaResponse> materiaResponses = materiasAtualizados.stream()
                .map(materia -> new MateriaResponse(materia.getMateriaId(), materia.getNomeMateria(), materia.getCurso().getCursoId())).toList();

        return materiaResponses;


}


    public List<MateriaResponse> getMateriasPorCurso(Long idCurso) {
        List<MateriaEntity> materias = materiaRepository.findAllByCursoId(idCurso);
        return materias.stream().map(
                materia ->new MateriaResponse(
                        materia.getMateriaId(),
                        materia.getNomeMateria(),
                        materia.getCurso().getCursoId()
                )).toList();
    }

    public void create(CursoRequest materiaRequest, String substring) {
    }
}
