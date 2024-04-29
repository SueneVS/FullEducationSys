package com.senai.fulleducationsys.service;

import com.senai.fulleducationsys.controller.dto.request.LoginRequest;
import com.senai.fulleducationsys.controller.dto.response.LoginResponse;
import com.senai.fulleducationsys.datasource.entity.UsuarioEntity;
import com.senai.fulleducationsys.datasource.repository.UsuarioRepository;
import com.senai.fulleducationsys.infra.exception.CustomException.CampoObrigatorioException;
import com.senai.fulleducationsys.infra.exception.CustomException.NotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j

public class TokenService {

    private final BCryptPasswordEncoder bCryptEncoder; 
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDencoder; 
    private final UsuarioRepository usuarioRepository;
    private static long TEMPO_EXPIRACAO = 3600000000L;

    public LoginResponse gerarToken (
            @RequestBody LoginRequest loginRequest
    ){

        UsuarioEntity usuarioEntity = usuarioRepository
                .findByLogin(loginRequest.login())
                .orElseThrow(
                        ()->{
                            log.error("Erro, usuário não existe");
                            return new NotFoundException("Erro, usuário não existe");
                        }
                );

        if(!usuarioEntity.senhaValida(loginRequest, bCryptEncoder)){
            log.error("Erro, senha incorreta");
            throw new CampoObrigatorioException("Erro, senha incorreta");
        }

        Instant now = Instant.now();

        String scope = usuarioEntity.getPapel().getNomePapel();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("FullEducationSys")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(TEMPO_EXPIRACAO))
                .subject(usuarioEntity.getUsuarioId().toString())
                .claim("scope", scope)
                .build();

        var valorJWT = jwtEncoder.encode(
                        JwtEncoderParameters.from(claims)
                )
                .getTokenValue();

        return new LoginResponse(valorJWT, TEMPO_EXPIRACAO);

    }

    public String buscaCampo(String token, String claim) {
        return jwtDencoder
                .decode(token)
                .getClaims()
                .get(claim)
                .toString();
    }
}
