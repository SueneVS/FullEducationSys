package com.senai.fulleducationsys.controller;


import com.senai.fulleducationsys.controller.dto.request.LoginRequest;
import com.senai.fulleducationsys.controller.dto.response.LoginResponse;
import com.senai.fulleducationsys.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j

public class TokenController {

    private final TokenService tokenService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> gerarToken (
            @RequestBody LoginRequest loginRequest
    ){

        LoginResponse response = tokenService.gerarToken(loginRequest);

        return ResponseEntity.ok(response);

    }
}
