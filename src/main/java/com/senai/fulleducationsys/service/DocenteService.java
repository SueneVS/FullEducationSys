package com.senai.fulleducationsys.service;

import com.senai.fulleducationsys.controller.dto.request.DocenteRequest;

import com.senai.fulleducationsys.controller.dto.response.DocenteResponse;

import com.senai.fulleducationsys.datasource.entity.DocenteEntity;

import com.senai.fulleducationsys.datasource.entity.PapelEntity;
import com.senai.fulleducationsys.datasource.entity.UsuarioEntity;
import com.senai.fulleducationsys.datasource.repository.DocenteRepository;
import com.senai.fulleducationsys.datasource.repository.PapelRepository;
import com.senai.fulleducationsys.datasource.repository.UsuarioRepository;
import com.senai.fulleducationsys.infra.exception.CustomException.CampoObrigatorioException;
import com.senai.fulleducationsys.infra.exception.CustomException.NotFoundException;
import com.senai.fulleducationsys.infra.exception.CustomException.UsuarioNaoAutorizadoException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DocenteService {
    private final DocenteRepository docenteRepository;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;

    public DocenteResponse create(DocenteRequest docenteRequest, String token) {

        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        if (docenteRequest.nome() == null
                || docenteRequest.dataEntrada() == null
                || docenteRequest.usuarioId() == null) {
            ArrayList<String> erros = new ArrayList<>();

            if(docenteRequest.nome() == null){
                erros.add("O campo 'nome' é obrigatorio");
            }
            if(docenteRequest.dataEntrada() == null){
                erros.add("O campo 'dataEntrada' é obrigatorio");
            }
            if(docenteRequest.usuarioId() == null) {
                erros.add("O campo 'usuarioId' é obrigatorio");
            }

            throw new CampoObrigatorioException(erros.toString());
        }

        UsuarioEntity usuario = usuarioRepository.findById(docenteRequest.usuarioId())
                .orElseThrow(() -> {
                    log.error("Erro, usuario não encontrado");
                    return new NotFoundException("Docente não encontrado com o ID: " + docenteRequest.usuarioId());
            });

        DocenteEntity docenteEntity = new DocenteEntity();
        docenteEntity.setUsuario(usuario);
        docenteEntity.setNome(docenteRequest.nome());
        docenteEntity.setDataEntrada(docenteRequest.dataEntrada());
        DocenteEntity docenteRegistrado = docenteRepository.save((docenteEntity));

        log.info("Criando docente-> Salvo com sucesso");

        return new DocenteResponse(docenteRegistrado.getDocenteId(),
                docenteRegistrado.getNome(), docenteRegistrado.getDataEntrada(), docenteRegistrado.getUsuario().getPapel().getNomePapel());
    }

    public DocenteResponse  getEntityIdDto(Long id, String token) {
        String papel = tokenService.buscaCampo(token, "scope");

        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO") && !papel.equals("RECRUITER")) {
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        Optional<DocenteEntity> docenteId = docenteRepository.findById(id);

        if (docenteId.isEmpty()) {
            log.error("Erro, usuário não existe");
            throw new NotFoundException("Não há docente cadastrado com o ID fornecido: " + id);
        }

        DocenteEntity docente = docenteId.get();

        if (papel.equals("ADM")) {
        } else if (papel.equals("PEDAGOGICO") || papel.equals("RECRUITER")) {
            Optional<PapelEntity> papelProfessor = papelRepository.findByNomePapel("PROFESSOR");

            if (papelProfessor.isPresent()) {
                PapelEntity papelEntity = papelProfessor.get();
                if (!docente.getUsuario().getPapel().equals(papelEntity)) {
                    throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
                }
            } else {
                log.error("Erro, papel 'PROFESSOR' não encontrado.");
                throw new NotFoundException("Papel 'PROFESSOR' não encontrado.");
            }
        } else {
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        log.info("Buscando docente por id ({}) -> Encontrado", id);
        return new DocenteResponse(docente.getDocenteId(), docente.getNome(), docente.getDataEntrada(), docente.getUsuario().getPapel().getNomePapel());
    }

    public  DocenteResponse update(Long id, DocenteRequest docenteRequest, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM") && !papel.equals("PEDAGOGICO") && !papel.equals("RECRUITER")) {
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        Optional<DocenteEntity> docenteId = docenteRepository.findById(id);

        if (docenteId.isEmpty()) {
            log.error("Erro, usuário não existe");
            throw new NotFoundException("Não há docente cadastrado com o ID fornecido: " + id);
        }

        DocenteEntity docente = docenteId.get();

        if (papel.equals("ADM")) {
        } else if (papel.equals("PEDAGOGICO") || papel.equals("RECRUITER")) {
            Optional<PapelEntity> papelProfessor = papelRepository.findByNomePapel("PROFESSOR");

            if (papelProfessor.isPresent()) {
                PapelEntity papelEntity = papelProfessor.get();
                if (!docente.getUsuario().getPapel().equals(papelEntity)) {
                    throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
                }
            } else {
                log.error("Erro, papel 'PROFESSOR' não encontrado.");
                throw new NotFoundException("Papel 'PROFESSOR' não encontrado.");
            }
        } else {
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        if (docenteRequest.nome() == null
                || docenteRequest.dataEntrada() == null
                || docenteRequest.usuarioId() == null) {
            ArrayList<String> erros = new ArrayList<>();

            if(docenteRequest.nome() == null){
                erros.add("O campo 'nome' é obrigatorio");
            }
            if(docenteRequest.dataEntrada() == null){
                erros.add("O campo 'dataEntrada' é obrigatorio");
            }
            if(docenteRequest.usuarioId() == null) {
                erros.add("O campo 'usuarioId' é obrigatorio");
            }

            throw new CampoObrigatorioException(erros.toString());
        }

        DocenteEntity docenteAtualizado = docenteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Erro, usuário não existe");
                    return new NotFoundException("Docente não encontrado com o ID: " + id);}
                );

        docenteAtualizado.setNome(docenteRequest.nome());
        docenteAtualizado.setDataEntrada(docenteRequest.dataEntrada());

        docenteRepository.save(docenteAtualizado);

        log.info("Alterando docente -> Salvo com sucesso");

        return new DocenteResponse(docenteAtualizado.getDocenteId(), docenteAtualizado.getNome(), docenteAtualizado.getDataEntrada(), docenteAtualizado.getUsuario().getPapel().getNomePapel());
    }

    public List<DocenteResponse> delete(Long id, String token) {
        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }
        DocenteEntity docenteDeletado = docenteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Erro, usuário não existe");
                    return new NotFoundException("Docente não encontrado com o ID: " + id);}
                );

        docenteRepository.delete(docenteDeletado);
        log.info("Excluindo docente com id ({}) -> Excluído com sucesso", id);

        List<DocenteEntity> docentesAtualizados = docenteRepository.findAll();

        List<DocenteResponse> docenteResponses = docentesAtualizados.stream()
                .map(docente -> new DocenteResponse(docente.getDocenteId(), docente.getNome(), docente.getDataEntrada(), docente.getUsuario().getPapel().getNomePapel())).toList();

        return docenteResponses;

    }

    public List<DocenteResponse> getEntityDto(String token) {
        String papel =  tokenService.buscaCampo(token, "scope");

        List<DocenteEntity> docentes;
        if (papel.equals("ADM")) {
            docentes = docenteRepository.findAll();
        } else if (papel.equals("PEDAGOGICO") || papel.equals("RECRUITER")) {
            Optional<PapelEntity> papelProfessor = papelRepository.findByNomePapel("PROFESSOR");

            if (papelProfessor.isPresent()) {
                docentes = docenteRepository.findByUsuarioPapel(Optional.of(papelProfessor.get()));
            } else {
                log.error("Erro, papel 'PROFESSOR' não encontrado.");
                throw new NotFoundException("Papel 'PROFESSOR' não encontrado.");
            }
        } else {
            log.error("Erro, Acesso não autorizado.");
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");

        }

        if(docentes.isEmpty()){
            log.error("Erro, usuários não existem");
            throw new NotFoundException("Não há docentes cadastrados.");
        }
        log.info("Buscando todos os docentes -> {} Encontrados", docentes.size());

        return docentes.stream().map(
                docente -> new DocenteResponse(docente.getDocenteId(), docente.getNome(), docente.getDataEntrada(), docente.getUsuario().getPapel().getNomePapel()))
                .collect(Collectors.toList());
    }
}
