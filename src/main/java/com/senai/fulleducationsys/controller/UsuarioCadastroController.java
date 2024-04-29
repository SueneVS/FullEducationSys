package com.senai.fulleducationsys.controller;

import com.senai.fulleducationsys.controller.dto.request.InserirUsuarioRequest;
import com.senai.fulleducationsys.controller.dto.response.UsuarioResponse;
import com.senai.fulleducationsys.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UsuarioCadastroController {
    private final UsuarioService usuarioService;

    @PostMapping("/cadastros")
    public ResponseEntity<UsuarioResponse> novoUsuario(
            @Validated @RequestBody InserirUsuarioRequest inserirUsuarioRequest,
            @RequestHeader(name = "Authorization") String token
            ) {

        UsuarioResponse  usuarioResponse = usuarioService.cadastraNovoUsuario(inserirUsuarioRequest, token.substring(7));

        return ResponseEntity.ok(usuarioResponse);
    }

}
