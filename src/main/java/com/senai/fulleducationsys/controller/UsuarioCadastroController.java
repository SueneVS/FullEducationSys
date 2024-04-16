package com.senai.fulleducationsys.controller;

import com.senai.fulleducationsys.controller.dto.request.InserirUsuarioRequest;
import com.senai.fulleducationsys.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UsuarioCadastroController {
    private final UsuarioService usuarioService;

    @PostMapping("/cadastro")
    public ResponseEntity<String> novoUsuario(
            @Validated @RequestBody InserirUsuarioRequest inserirUsuarioRequest,
            @RequestHeader(name = "Authorization") String token
            ) {

        usuarioService.cadastraNovoUsuario(inserirUsuarioRequest, token.substring(7));

        return ResponseEntity.ok("Usuario Salvo!");
    }

}
