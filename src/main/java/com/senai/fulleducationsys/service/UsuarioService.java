package com.senai.fulleducationsys.service;

import com.senai.fulleducationsys.controller.dto.request.InserirUsuarioRequest;
import com.senai.fulleducationsys.datasource.entity.UsuarioEntity;
import com.senai.fulleducationsys.datasource.repository.PapelRepository;
import com.senai.fulleducationsys.datasource.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.SpringVersion;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@Slf4j

public class UsuarioService {
    private final BCryptPasswordEncoder bCryptEncoder;
    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;
    private final TokenService tokenService;

    public void cadastraNovoUsuario(
          InserirUsuarioRequest inserirUsuarioRequest, String token
    ) {

        String papel =  tokenService.buscaCampo(token, "scope");
        if (!papel.equals("ADM")){
            throw new RuntimeException("Acesso não autorizado.");
        }

        boolean usuarioExsite = usuarioRepository
                .findByLogin(inserirUsuarioRequest.login())
                .isPresent();

        if (usuarioExsite) {
            throw new RuntimeException("Usuario já existe");
        }

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setLogin(inserirUsuarioRequest.login());;
        usuario.setSenha(bCryptEncoder.encode(inserirUsuarioRequest.senha()));
        usuario.setPapel(papelRepository.findByNome(inserirUsuarioRequest.nomePapel())
                        .orElseThrow(() -> new RuntimeException("Papel inválido ou inexistente"))
        );

        usuarioRepository.save(usuario);
    }
}
