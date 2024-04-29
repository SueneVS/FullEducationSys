package com.senai.fulleducationsys.service;

import com.senai.fulleducationsys.controller.dto.request.InserirUsuarioRequest;
import com.senai.fulleducationsys.controller.dto.response.UsuarioResponse;
import com.senai.fulleducationsys.datasource.entity.PapelEntity;
import com.senai.fulleducationsys.datasource.entity.UsuarioEntity;
import com.senai.fulleducationsys.datasource.repository.PapelRepository;
import com.senai.fulleducationsys.datasource.repository.UsuarioRepository;
import com.senai.fulleducationsys.infra.exception.CustomException.CadastroDuplicadoException;
import com.senai.fulleducationsys.infra.exception.CustomException.CampoObrigatorioException;
import com.senai.fulleducationsys.infra.exception.CustomException.UsuarioNaoAutorizadoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.SpringVersion;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j

public class UsuarioService {
    private final BCryptPasswordEncoder bCryptEncoder;
    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;
    private final TokenService tokenService;


    public UsuarioResponse cadastraNovoUsuario(InserirUsuarioRequest inserirUsuarioRequest, String token) {

        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new UsuarioNaoAutorizadoException("Acesso não autorizado.");
        }

        if (inserirUsuarioRequest.login() == null
                || inserirUsuarioRequest.senha() == null
                || inserirUsuarioRequest.nomePapel() == null){
            ArrayList<String> erros = new ArrayList<>();

            if(inserirUsuarioRequest.login() == null){
                erros.add("O campo 'login' é obrigatorio");
            }
            if(inserirUsuarioRequest.senha() == null){
                erros.add("O campo 'senha' é obrigatorio");
            }
            if(inserirUsuarioRequest.nomePapel() == null) {
                erros.add("O campo 'nomePapel' é obrigatorio");
            }

            throw new CampoObrigatorioException(erros.toString());
        }

        boolean usuarioExiste = usuarioRepository
                .findByLogin(inserirUsuarioRequest.login())
                .isPresent();

        if (usuarioExiste) {
            throw new CadastroDuplicadoException("Usuario já existe");
        }

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setLogin(inserirUsuarioRequest.login());;
        usuario.setSenha(bCryptEncoder.encode(inserirUsuarioRequest.senha()));
        usuario.setPapel(papelRepository.findByNomePapel(inserirUsuarioRequest.nomePapel())
                        .orElseThrow(() -> new RuntimeException("Papel inválido ou inexistente"))
        );
        log.info("Criando usuario-> Salvo com sucesso");
        usuarioRepository.save(usuario);

        return new UsuarioResponse(usuario.getUsuarioId(), usuario.getLogin(), usuario.getPapel().getNomePapel());
    }
}
