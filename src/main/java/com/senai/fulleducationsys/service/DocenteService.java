package com.senai.fulleducationsys.service;

import com.senai.fulleducationsys.controller.dto.request.DocenteRequest;

import com.senai.fulleducationsys.controller.dto.response.DocenteResponse;

import com.senai.fulleducationsys.datasource.entity.DocenteEntity;

import com.senai.fulleducationsys.datasource.entity.UsuarioEntity;
import com.senai.fulleducationsys.datasource.repository.DocenteRepository;
import com.senai.fulleducationsys.datasource.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DocenteService {
    private final DocenteRepository docenteRepository;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public DocenteResponse create(DocenteRequest docenteRequest, String token) {

        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new RuntimeException("Acesso não autorizado.");
        }

        UsuarioEntity usuario = usuarioRepository.findById(docenteRequest.usuarioId())
                .orElseThrow(() -> new RuntimeException("Docente não encontrado com o ID:" + docenteRequest.usuarioId()));

        DocenteEntity docenteEntity = new DocenteEntity();
        docenteEntity.setUsuario(usuario);
        docenteEntity.setNome(docenteRequest.nome());
        docenteEntity.setDataEntrada(docenteRequest.dataEntrada());
        DocenteEntity docenteRegistrado = docenteRepository.save((docenteEntity));

        return new DocenteResponse(docenteRegistrado.getDocenteId(),
                docenteRegistrado.getNome(), docenteRegistrado.getDataEntrada(), docenteRegistrado.getUsuario().getUsuarioId());
    }

    public  DocenteResponse getEntityIdDto(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO") && !papel.equals("RECRUITER")){
            throw new RuntimeException("Acesso não autorizado.");
        }

    DocenteEntity docenteId = docenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente não encontrado com o ID: " + id));
        return new DocenteResponse(docenteId.getDocenteId(), docenteId.getNome(), docenteId.getDataEntrada(), docenteId.getUsuario().getUsuarioId());
    }

    public  DocenteResponse update(Long id, DocenteRequest docenteRequest, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO") && !papel.equals("RECRUITER")){
            throw new RuntimeException("Acesso não autorizado.");
        }

        DocenteEntity docenteAtualizado = docenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente não encontrado com o ID: " + id));

        docenteAtualizado.setNome(docenteRequest.nome());

        docenteRepository.save(docenteAtualizado);

        return new DocenteResponse(docenteAtualizado.getDocenteId(), docenteAtualizado.getNome(), docenteAtualizado.getDataEntrada(), docenteAtualizado.getUsuario().getUsuarioId());
    }

    public List<DocenteResponse> delete(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new RuntimeException("Acesso não autorizado.");
        }
        DocenteEntity docenteDeletado = docenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente não encontrado com o ID: " + id));

        docenteRepository.delete(docenteDeletado);

        List<DocenteEntity> docentesAtualizados = docenteRepository.findAll();

        List<DocenteResponse> docenteResponses = docentesAtualizados.stream()
                .map(docente -> new DocenteResponse(docente.getDocenteId(), docente.getNome(), docente.getDataEntrada(), docente.getUsuario().getUsuarioId())).toList();

        return docenteResponses;

    }

    public List<DocenteResponse> getEntityDto(String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO") && !papel.equals("RECRUITER")){
            throw new RuntimeException("Acesso não autorizado.");
        }

        List<DocenteEntity> docentes = docenteRepository.findAll();

        if(docentes.isEmpty()){
            throw new RuntimeException("Não há docentes cadastrados.");
        }

        return docentes.stream().map(
                docente -> new DocenteResponse(docente.getDocenteId(), docente.getNome(), docente.getDataEntrada(), docente.getUsuario().getUsuarioId())).toList();
    }
}
